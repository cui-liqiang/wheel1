package testpackage;

import core.annotation.Component;
import core.annotation.Qualified;

import javax.inject.Inject;

@Component("beanWithQualified")
public class BeanWithQualified {
    private IBeanWithId id;
    @Inject @Qualified(id="id2")
    private IBeanWithId id2;
    private IBeanWithId id3;

    @Inject public BeanWithQualified(@Qualified(id="beanWithId") IBeanWithId id) {
        this.id = id;
    }

    @Inject @Qualified(id="id3")
    public void setId3(IBeanWithId id3) {
        this.id3 = id3;
    }

    public IBeanWithId getId3() {
        return id3;
    }

    public IBeanWithId getId() {
        return id;
    }

    public IBeanWithId getId2() {
        return id2;
    }

    public void setId2(IBeanWithId id2) {
        this.id2 = id2;
    }
}
