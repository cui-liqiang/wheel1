package core;

import core.annotation.Component;
import core.scopes.Prototype;

public class AnnotatedBeanDefinition extends SimpleBeanDefinition {

    public AnnotatedBeanDefinition(Class clazz) throws Exception {
        super(clazz, clazz.isAnnotationPresent(Prototype.class));
        extractMetaData();
    }

    private void extractMetaData() throws Exception {
        extractId();
        extractInjectField();
        extractInjectSetter();
    }

    private void extractId() {
        String id = ((Component) clazz.getAnnotation(Component.class)).value();
        this.id = id.equals("") ? clazz.getName() + "instance" : id;
    }

}
