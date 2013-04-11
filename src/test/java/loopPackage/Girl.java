package loopPackage;

import core.annotation.Component;

import javax.inject.Inject;

@Component
public class Girl {

    @Inject
    public Girl(Boy boy) {
    }
}
