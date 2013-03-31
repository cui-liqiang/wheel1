package core;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BeanDefinition {
    private Class clazz;
    private IocContainer container;
    private Object instance = null;

    public BeanDefinition(Class clazz, IocContainer container) {
        this.clazz = clazz;
        this.container = container;
    }

    public void init() throws Exception {
        if(isInitialized()) return;
        initBean();
    }

    private void initBean() throws Exception {
        Constructor[] constructors = filterWithInjectAnnotation(clazz.getConstructors());
        if(constructors.length == 0) {
            instance = clazz.newInstance();
        } else if(constructors.length == 1) {
            initInstanceWithConstructor(constructors[0]);
        } else {
            throw new Exception("classes registered in IOC container must have one or zero constructor, but "
                    + clazz.getName() +
                    " have " + constructors.length);
        }
    }

    private Constructor[] filterWithInjectAnnotation(Constructor[] constructors) {
        List<Constructor> filtered = new ArrayList<Constructor>();
        for (Constructor constructor : constructors) {
            if(constructor.isAnnotationPresent(Inject.class)) {
                filtered.add(constructor);
            }
        }
        return filtered.toArray(new Constructor[0]);
    }

    private void initInstanceWithConstructor(Constructor constructor) throws Exception {
        Class[] parameterTypes = constructor.getParameterTypes();
        List<Object> params = new ArrayList<Object>();
        for (Class type : parameterTypes) {
            params.add(container.getBean(type));
        }
        initInstanceWithParams(params, constructor);
    }

    private void initInstanceWithParams(List<Object> params, Constructor constructor) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        switch (params.size()) {
            case 0 : instance = constructor.newInstance(); break;
            case 1 : instance = constructor.newInstance(params.get(0)); break;
            case 2 : instance = constructor.newInstance(params.get(0), params.get(1)); break;
            case 3 : instance = constructor.newInstance(params.get(0), params.get(1), params.get(2)); break;
            case 4 : instance = constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3)); break;
            case 5 : instance = constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4)); break;
            case 6 : instance = constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5)); break;
            case 7 : instance = constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5), params.get(6)); break;
            case 8 : instance = constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5), params.get(6), params.get(7)); break;
            case 9 : instance = constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5), params.get(6), params.get(7), params.get(8)); break;
            case 10 : instance = constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5), params.get(6), params.get(7), params.get(8), params.get(9)); break;
        }
    }

    public boolean isInitialized() {
        return instance != null;
    }

    public boolean assignableTo(Class type) {
        return type.isAssignableFrom(clazz);
    }

    public Object getBean() throws Exception {
        if(!isInitialized())
            initBean();
        return instance;
    }
}
