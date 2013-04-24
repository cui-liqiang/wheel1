package core;

import org.junit.Test;
import testpackage.OtherToRegister;
import testpackage.ToRegister;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class IocContainerWithRegisterTest {
    IocContainer container;

    public IocContainerWithRegisterTest() throws Exception {
        container = new IocContainerBuilder().build();
    }

    @Test
    public void should_register_class_directly() throws Exception {
        container.register(ToRegister.class);
        ToRegister bean = container.getBean(ToRegister.class);
        assertEquals(bean.getClass(), ToRegister.class);
    }

    @Test
    public void should_register_multiple_class_directly() throws Exception {
        container.register(ToRegister.class);
        container.register(OtherToRegister.class);

        ToRegister bean = container.getBean(ToRegister.class);
        assertEquals(bean.getClass(), ToRegister.class);

        OtherToRegister otherBean = container.getBean(OtherToRegister.class);
        assertEquals(otherBean.getClass(), OtherToRegister.class);
    }

    @Test
    public void should_be_same_if_prototype_is_false() throws Exception {
        container.register(ToRegister.class);
        ToRegister bean = container.getBean(ToRegister.class);

        assertSame(bean, container.getBean(ToRegister.class));
    }

    @Test
    public void should_be_not_same_if_prototype_is_true() throws Exception {
        container.register(ToRegister.class, true);
        ToRegister bean = container.getBean(ToRegister.class);

        assertNotSame(bean, container.getBean(ToRegister.class));
    }
}
