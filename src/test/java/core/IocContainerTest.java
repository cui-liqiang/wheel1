package core;

import org.junit.Test;
import testpackage.Demo1;
import testpackage.InjectAnnotationOnConstructor;
import testpackage.SetterInject;
import testpackage.SetterInjectOnMethod;
import testpackage.nested.Demo2;

import static org.junit.Assert.assertEquals;

public class IocContainerTest {
    IocContainer container;

    public IocContainerTest() throws Exception {
        container = new IocContainer("testpackage");
    }

    @Test
    public void should_init_container_with_classes_in_package() throws Exception {
        Demo1 bean = container.getBean(Demo1.class);
        assertEquals(Demo2.TEST_VALUE, bean.getDemo2Value());
    }

    @Test
    public void should_only_use_the_constructor_with_inject_annotation() throws Exception {
        InjectAnnotationOnConstructor bean = container.getBean(InjectAnnotationOnConstructor.class);

        assertEquals(InjectAnnotationOnConstructor.TEST_VALUE, bean.getValue());
    }

    @Test
    public void should_inject_field_from_setter_if_annotated_on_field() throws Exception {
        SetterInject bean = container.getBean(SetterInject.class);
        assertEquals(Demo2.TEST_VALUE, bean.getDemo().getValue());
    }

    @Test
    public void should_inject_field_from_setter_if_annotated_on_setter() throws Exception {
        SetterInjectOnMethod bean = container.getBean(SetterInjectOnMethod.class);
        assertEquals(Demo2.TEST_VALUE, bean.getDemo().getValue());
    }
}
