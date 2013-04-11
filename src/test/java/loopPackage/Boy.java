package loopPackage;

import core.annotation.Component;

import javax.inject.Inject;

@Component
public class Boy {

    @Inject
    public Boy(Girl girl) {
    }
}
