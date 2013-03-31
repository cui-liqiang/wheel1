package core;

import org.junit.Test;
import testpackage.Demo1;
import testpackage.InjectAnnotationOnConstructor;
import testpackage.nested.Demo2;

import static org.junit.Assert.assertEquals;

public class IocContainerTest {
    IocContainer container;

    public IocContainerTest() throws Exception {
        container = new IocContainer("testpackage");
    }

    @Test
    public void should_init_container_with_classes_in_package() throws Exception {
        Demo1 bean = (Demo1)container.getBean(Demo1.class);
        assertEquals(Demo2.TEST_VALUE, bean.getDemo2Value());
    }

    @Test
    public void should_only_use_the_constructor_with_inject_annotation() throws Exception {
        InjectAnnotationOnConstructor bean = (InjectAnnotationOnConstructor)container.getBean(InjectAnnotationOnConstructor.class);

        assertEquals(InjectAnnotationOnConstructor.TEST_VALUE, bean.getValue());
    }
}
