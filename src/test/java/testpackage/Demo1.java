package testpackage;

import core.annotation.Component;
import testpackage.nested.Demo2;

@Component
public class Demo1 {
    private Demo2 demo2;

    public Demo1(Demo2 demo2) {
        this.demo2 = demo2;
    }

    public int getDemo2Value() {
        return demo2.getValue();
    }
}
