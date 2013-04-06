package core;

import core.bean.parsers.XmlBeanDefinitionParser;
import core.classfilter.ClassFilter;
import core.classfilter.ClazzAnnotationFilter;
import util.ClassPathUtil;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class IocContainer {
    private List<BeanDefinition> definitions = new LinkedList<BeanDefinition>();
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
        definitions.addAll(parser.parse(configFile));
    }

    public <T> T getBean(Class<T> type) throws Exception {
        if(Modifier.isAbstract(type.getModifiers()) || type.isInterface()) throw new Exception("Cannot get bean with type of Abstract class or Interface");
        return (T) getBeanByCompatibleType(type);
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
            definitions.add(new AnnotatedBeanDefinition(clazz));
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
