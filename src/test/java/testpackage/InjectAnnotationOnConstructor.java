package testpackage;

import core.annotation.Component;
import javax.inject.Inject;

@Component
public class InjectAnnotationOnConstructor {
    public static int TEST_VALUE = 2;

    private int value = 1;

    public InjectAnnotationOnConstructor(int i) {
    }

    @Inject public InjectAnnotationOnConstructor() {
        value = TEST_VALUE;
    }

    public int getValue() {
        return value;
    }
}
