package core.annotation;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Qualifier
@Retention(RUNTIME)
@Target({ PARAMETER, METHOD, FIELD })
public @interface Qualified {
    String id();
}
