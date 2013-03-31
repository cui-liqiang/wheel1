package testpackage;

import core.annotation.Component;
import javax.inject.Inject;

@Component
public class SetterInjectOnMethod {
    Demo2Inter demo;

    public Demo2Inter getDemo() {
        return demo;
    }

    @Inject
    public void setDemo(Demo2Inter demo) {
        this.demo = demo;
    }
}
