package core.classfilter;

import core.annotation.Component;

public class ClazzAnnotationFilter implements ClassFilter {
    @Override
    public boolean match(Class clazz) {
        return clazz.isAnnotationPresent(Component.class);
    }
}
