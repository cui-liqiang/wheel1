package core;

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
    private IocContainer parent = NullIocContainer.getInstance();

    IocContainer(String packageName, String configFile, IocContainer parent) throws Exception {
        if(parent != null) this.parent = parent;

        initAnnotatedBeanDefinitions(packageName);
        initXmlBeanDefinitions(configFile);

        for (BeanDefinition definition : definitions) {
            definition.init(this);
        }
    }

    public <T> T getBean(Class<T> type) throws Exception {
        return (T) getExactMatchTypeBean(type);
    }

    public Object getBeanByCompatibleType(final Class type) throws Exception {
        if(type.isPrimitive())
            throw new Exception("Cannot inject primitive from container. Try use xml way to config");

        return getUniqueMatchObject("type " + type.getName(), new Predicate<BeanDefinition>() {
            @Override
            public boolean matches(BeanDefinition element) {
                return element.assignableTo(type);
            }
        });
    }

    public Object getBeanById(final String ref) throws Exception {
        return getUniqueMatchObject("id " + ref, new Predicate<BeanDefinition>() {
            @Override
            public boolean matches(BeanDefinition definition) {
                return definition.matchId(ref);
            }
        });
    }

    protected Object getUniqueMatchObject(String searchFor, Predicate predicate) throws Exception {
        Object foundObject = null;
        for (BeanDefinition definition : definitions) {
            if(predicate.matches(definition)) {
                if(foundObject == null)
                    foundObject = definition.getBean(this);
                else
                    throw new Exception("Found more than one candidates for " + searchFor + ". Cannot determine which one to use!");
            }
        }
        if(foundObject == null) {
            return parent.getUniqueMatchObject(searchFor, predicate);
        }
        return foundObject;
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

    private void initXmlBeanDefinitions(String configFile) throws Exception {
        if(configFile == null) return;
        addAllWithIdCheck(parser.parse(configFile));
    }

    private Object getExactMatchTypeBean(final Class<?> type) throws Exception {
        return getUniqueMatchObject("type " + type.getName(), new Predicate<BeanDefinition>() {
            @Override
            public boolean matches(BeanDefinition element) {
                return element.exactMatchType(type);
            }
        });
    }

    private void initAnnotatedBeanDefinitions(String packageName) throws Exception {
        for (String className : ClassPathUtil.getClassNamesInPackage(packageName)) {
            Class<?> clazz = Class.forName(className);
            if(annotationFilter.match(clazz)) continue;
            addWithIdCheck(new AnnotatedBeanDefinition(clazz));
        }
    }

    interface Predicate<T> {
        public boolean matches(T element);

    }
}
