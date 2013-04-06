package core;

import core.bean.parsers.XmlBeanDefinitionParser;
import core.classfilter.ClassFilter;
import core.classfilter.ClazzAnnotationFilter;
import util.ClassPathUtil;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IocContainer {
    private Set<BeanDefinition> definitions = new HashSet<BeanDefinition>();
    private ClassFilter annotationFilter = new ClazzAnnotationFilter();
    private XmlBeanDefinitionParser parser = new XmlBeanDefinitionParser();

    public IocContainer(String packageName) throws Exception {
        initAnnotatedBeanDefinitions(packageName);
    }

    public IocContainer(String packageName, String configFile) throws Exception {
        initAnnotatedBeanDefinitions(packageName);
        initXmlBeanDefinitions(configFile);
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

    private Object getExactMatchTypeBean(Class<?> type) throws Exception {
        for (BeanDefinition definition : definitions) {
            if(definition.exactMatchType(type)) {
                return definition.getBean(this);
            }
        }
        throw new Exception("Cannot find a bean with type " + type.getName() + " existing. Maybe you forget to annotate it with @Component?");
    }

    public Object getBeanByCompatibleType(Class type) throws Exception {
        for (BeanDefinition definition : definitions) {
            if(definition.assignableTo(type)) {
                return definition.getBean(this);
            }
        }
        throw new Exception("Cannot find a bean with type " + type.getName() + " existing. Maybe you forget to annotate it with @Component?");
    }

    private void initAnnotatedBeanDefinitions(String packageName) throws Exception {
        for (String className : ClassPathUtil.getClassNamesInPackage(packageName)) {
            Class<?> clazz = Class.forName(className);
            if(annotationFilter.match(clazz)) continue;
            addWithIdCheck(new AnnotatedBeanDefinition(clazz));
        }

        for (BeanDefinition definition : definitions) {
            definition.init(this);
        }
    }

    public Object getBeanById(String ref) throws Exception {
        for (BeanDefinition definition : definitions) {
            if(definition.matchId(ref)) {
                return definition.getBean(this);
            }
        }
        throw new Exception("Cannot find a bean with id " + ref + " existing. Maybe you forget to config it in xml or in @Component");
    }
}
