package core;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IocContainerWithLoopTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void should_throw_exception_if_loop_exist() throws Exception {
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("loop!");

        new IocContainerBuilder().withPackageName("loopPackage").build();
    }

}
