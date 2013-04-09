package core;

import org.junit.Before;
import org.junit.Test;
import testpackage.controllers.ControllerA;
import testpackage.services.ServiceA;

import static org.junit.Assert.assertTrue;

public class ContainerScopeTest {
    private IocContainer services;
    private IocContainer controllers;

    @Before
    public void setUp() throws Exception {
        services = new IocContainerBuilder().withPackageName("testpackage.services").build();
        controllers = new IocContainerBuilder().withPackageName("testpackage.controllers")
                                               .withParent(services)
                                               .build();

    }

    @Test
    public void container_should_be_able_to_access_its_parents_beans() throws Exception {
        Object o = controllers.getBeanById("controllerA");

        assertTrue(o instanceof ControllerA);
        ControllerA controller = (ControllerA)o;

        assertTrue(controller.getService() instanceof ServiceA);
    }

    @Test(expected = Exception.class)
    public void container_should_not_be_able_to_access_its_child_beans() throws Exception {
        services.getBeanById("controllerA");
    }
}
