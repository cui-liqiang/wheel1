package core;

import org.junit.Test;
import testpackage.Demo1;
import testpackage.ToRegister;
import testpackage.nested.Demo2;

import static org.junit.Assert.assertEquals;

public class IocContainerWithRegisterTest {
    IocContainer container;

    public IocContainerWithRegisterTest() throws Exception {
        container = new IocContainerBuilder().build();
    }

    @Test
    public void should_init_container_with_classes_in_package() throws Exception {
        container.register(ToRegister.class);
        ToRegister bean = container.getBean(ToRegister.class);
        assertEquals(bean.getClass(), ToRegister.class);
    }
}
