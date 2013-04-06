package core;

import org.junit.Before;
import org.junit.Test;
import testpackage.Demo1;
import testpackage.NoComponentAnnotation;
import testpackage.SetterInject;
import testpackage.nested.Demo2;

import static org.junit.Assert.*;

public class IocContainerWithXmlConfigTest {
    private IocContainer container;

    @Before
    public void setUp() throws Exception {
        container = new IocContainerBuilder().withConfigFile("demo2-bean-definition.xml").build();
    }

    @Test
    public void should_create_bean_with_primitive_constructor_param() throws Exception {
        Object o = container.getBeanById("NoComponent");
        assertTrue(o instanceof NoComponentAnnotation);

        NoComponentAnnotation bean = (NoComponentAnnotation) o;

        assertEquals(5, bean.getValue());
    }

    @Test
    public void should_create_bean_with_bean_constructor_param() throws Exception {
        Object o = container.getBeanById("demo1");
        assertTrue(o instanceof Demo1);

        Demo1 bean = (Demo1) o;

        assertEquals(Demo2.TEST_VALUE, bean.getDemo2Value());
    }

    @Test
    public void should_create_bean_with_bean_setter_param() throws Exception {
        Object o = container.getBeanById("setterInject");
        assertTrue(o instanceof SetterInject);

        SetterInject bean = (SetterInject) o;

        assertEquals(Demo2.TEST_VALUE, bean.getDemo().getValue());
    }

    @Test
    public void should_extract_scope_information() throws Exception {
        Object o = container.getBeanById("prototype");
        assertNotSame(o, container.getBeanById("prototype"));
    }

    @Test
    public void should_be_singleton_scope_by_default() throws Exception {
        Object o = container.getBeanById("setterInject");
        assertSame(o, container.getBeanById("setterInject"));
    }
}
