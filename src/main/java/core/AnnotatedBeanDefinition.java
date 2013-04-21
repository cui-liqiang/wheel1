package core;

import core.annotation.Component;
import core.scopes.Prototype;

public class AnnotatedBeanDefinition extends SimpleBeanDefinition {

    public AnnotatedBeanDefinition(Class clazz) throws Exception {
        super(clazz);
        extractMetaData();
    }

    private void extractMetaData() throws Exception {
        extractId();
        extractScope();
        extractInjectField();
        extractInjectSetter();
    }

    private void extractScope() {
        prototypeScope = clazz.isAnnotationPresent(Prototype.class);
    }

    private void extractId() {
        String id = ((Component) clazz.getAnnotation(Component.class)).value();
        this.id = id.equals("") ? clazz.getName() + "instance" : id;
    }

}
