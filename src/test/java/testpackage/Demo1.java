package testpackage;

import core.annotation.Component;
import testpackage.nested.Demo2;

import javax.inject.Inject;

@Component
public class Demo1 {
    private Demo2Inter demo2;

    @Inject public Demo1(Demo2Inter demo2) {
        this.demo2 = demo2;
    }

    public int getDemo2Value() {
        return demo2.getValue();
    }
}
