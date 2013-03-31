package testpackage;

import core.annotation.Component;
import core.scopes.Prototype;

import javax.inject.Inject;

@Component
@Prototype
public class PrototypeTypeClass extends Demo1{
    @Inject public PrototypeTypeClass(Demo2Inter demo2) {
        super(demo2);
    }
}
