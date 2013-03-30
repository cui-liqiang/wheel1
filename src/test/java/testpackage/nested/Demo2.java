package testpackage.nested;

import core.annotation.Component;

@Component
public class Demo2 {
    public static final int TEST_VALUE = 6;

    public int getValue() {
        return TEST_VALUE;
    }
}
