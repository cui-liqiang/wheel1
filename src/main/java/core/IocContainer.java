package core;

import core.classfilter.ClassFilter;
import core.classfilter.ClazzAnnotationFilter;
import util.ClassPathUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IocContainer {
    private String packageName;
    private List<String> classNames = new ArrayList<String>();
    private List<BeanDefinition> definitions = new LinkedList<BeanDefinition>();
    private ClassFilter annotationFilter = new ClazzAnnotationFilter();

    public IocContainer(String packageName) throws Exception {
        this.packageName = packageName;
        init();
    }

    public Object getBean(Class type) throws Exception {
        for (BeanDefinition definition : definitions) {
            if(definition.assignableTo(type)) {
                return definition.getBean();
            }
        }
        throw new Exception("Cannot find a bean with type " + type.getName() + " existing. Maybe you forget to annotate it with @Component?");
    }

    private void init() throws Exception {
        classNames = ClassPathUtil.getClassNamesInPackage(packageName);
        for (String className : classNames) {
            Class<?> clazz = Class.forName(className);
            if(annotationFilter.match(clazz)) continue;

            definitions.add(new BeanDefinition(clazz, this));
        }

        for (BeanDefinition definition : definitions) {
            definition.init();
        }
    }
}
