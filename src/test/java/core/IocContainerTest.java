package core;

import org.junit.Test;
import testpackage.Demo1;
import testpackage.nested.Demo2;

import static org.junit.Assert.assertEquals;

public class IocContainerTest {
    @Test
    public void should_init_container_with_classes_in_package() throws Exception {
        IocContainer container = new IocContainer("testpackage");
        Demo1 bean = (Demo1)container.getBean(Demo1.class);
        assertEquals(Demo2.TEST_VALUE, bean.getDemo2Value());
    }
}
