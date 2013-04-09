package core;

import com.google.common.base.Predicates;
import core.bean.parsers.XmlBeanDefinitionParser;
import core.classfilter.ClassFilter;
import core.classfilter.ClazzAnnotationFilter;
import util.ClassPathUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IocContainer {
    private Set<BeanDefinition> definitions = new HashSet<BeanDefinition>();
    private ClassFilter annotationFilter = new ClazzAnnotationFilter();
    private XmlBeanDefinitionParser parser = new XmlBeanDefinitionParser();

    IocContainer(String packageName, String configFile) throws Exception {
        initAnnotatedBeanDefinitions(packageName);
        initXmlBeanDefinitions(configFile);

        for (BeanDefinition definition : definitions) {
            definition.init(this);
        }
    }

    private void initXmlBeanDefinitions(String configFile) throws Exception {
        if(configFile == null) return;
        addAllWithIdCheck(parser.parse(configFile));
    }

    private void addAllWithIdCheck(List<BeanDefinition> definitions) throws Exception {
        for (BeanDefinition definition : definitions) {
            addWithIdCheck(definition);
        }
    }

    private void addWithIdCheck(BeanDefinition definition) throws Exception {
        if(this.definitions.contains(definition)) {
            throw new Exception("Duplicated bean definition with id \"" + definition.id + "\"");
        }
        this.definitions.add(definition);
    }

    public <T> T getBean(Class<T> type) throws Exception {
        return (T) getExactMatchTypeBean(type);
    }

    private Object getExactMatchTypeBean(final Class<?> type) throws Exception {
        return getUniqueMatchObject(type, new Predicate<BeanDefinition>() {
            @Override
            public boolean matches(BeanDefinition element) {
                return element.exactMatchType(type);
            }
        });
    }

    public Object getBeanByCompatibleType(final Class type) throws Exception {
        if(type.isPrimitive())
            throw new Exception("Cannot inject primitive from container. Try use xml way to config");

        return getUniqueMatchObject(type, new Predicate<BeanDefinition>() {
            @Override
            public boolean matches(BeanDefinition element) {
                return element.assignableTo(type);
            }
        });
    }

    private Object getUniqueMatchObject(final Class type, Predicate predicate) throws Exception {
        Object foundObject = null;
        for (BeanDefinition definition : definitions) {
            if(predicate.matches(definition)) {
                if(foundObject == null)
                    foundObject = definition.getBean(this);
                else
                    throw new Exception("Found more than one candidates for type " + type.getName() + ". Cannot determine which one to use!");
            }
        }
        if(foundObject == null)
            throw new Exception("Cannot find a bean with type " + type.getName() + " existing. Maybe you forget to annotate it with @Component?");
        return foundObject;
    }

    interface Predicate<T> {
        public boolean matches(T element);
    }

    private void initAnnotatedBeanDefinitions(String packageName) throws Exception {
        for (String className : ClassPathUtil.getClassNamesInPackage(packageName)) {
            Class<?> clazz = Class.forName(className);
            if(annotationFilter.match(clazz)) continue;
            addWithIdCheck(new AnnotatedBeanDefinition(clazz));
        }
    }

    public Object getBeanById(String ref) throws Exception {
        for (BeanDefinition definition : definitions) {
            if(definition.matchId(ref)) {
                return definition.getBean(this);
            }
        }
        throw new Exception("Cannot find a bean with id " + ref + " . Maybe you forget to config it in xml or in @Component");
    }
}
