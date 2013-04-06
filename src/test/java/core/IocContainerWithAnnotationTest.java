package core;

import org.junit.Test;
import testpackage.*;
import testpackage.BeanWithId;
import testpackage.nested.Demo2;

import static org.junit.Assert.*;

public class IocContainerWithAnnotationTest {
    IocContainer container;

    public IocContainerWithAnnotationTest() throws Exception {
        container = new IocContainerBuilder().withPackageName("testpackage").build();
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

    @Test
    public void beans_should_be_singleton_by_default() throws Exception {
        assertSame(container.getBean(Demo1.class), container.getBean(Demo1.class));
    }

    @Test
    public void beans_should_be_different_instance_if_annotated_with_prototype() throws Exception {
        PrototypeTypeClass bean = container.getBean(PrototypeTypeClass.class);
        assertNotSame(bean, container.getBean(PrototypeTypeClass.class));
    }

    @Test
    public void should_be_able_to_specify_id_in_component_annotation() throws Exception {
        Object beanWithId = container.getBeanById("beanWithId");
        assertTrue(beanWithId instanceof BeanWithId);
    }

    @Test
    public void should_be_able_to_specify_qualified_id_in_cons_parameter() throws Exception {
        Object o = container.getBeanById("beanWithQualified");
        BeanWithQualified bean = (BeanWithQualified)o;

        assertSame(container.getBeanById("beanWithId"), bean.getId());
    }

    @Test
    public void should_be_able_to_specify_qualified_id_on_field() throws Exception {
        Object o = container.getBeanById("beanWithQualified");
        BeanWithQualified bean = (BeanWithQualified)o;

        assertSame(container.getBeanById("id2"), bean.getId2());
    }

    @Test
    public void should_be_able_to_specify_qualified_id_in_setter() throws Exception {
        Object o = container.getBeanById("beanWithQualified");
        BeanWithQualified bean = (BeanWithQualified)o;

        assertSame(container.getBeanById("id3"), bean.getId3());
    }
}
