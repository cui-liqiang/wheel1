package testpackage.nested;

import core.annotation.Component;
import testpackage.Demo2Inter;

@Component
public class Demo2 implements Demo2Inter{
    public static final int TEST_VALUE = 6;

    @Override
    public int getValue() {
        return TEST_VALUE;
    }
}
