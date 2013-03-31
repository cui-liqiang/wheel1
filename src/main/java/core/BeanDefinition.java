package core;

import com.sun.xml.internal.ws.util.StringUtils;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        initConstructorInjection();
        initSetterInjectionOnField();
        initSetterInjectionOnSetter();
    }

    private void initSetterInjectionOnSetter() throws Exception {
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if(declaredMethod.isAnnotationPresent(Inject.class)) {
                if(declaredMethod.getName().matches("set[A-Z].*]")) {
                   throw new Exception(declaredMethod.getName() + " doesn't look like a setter");
                }
                Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                if(parameterTypes.length != 1) {
                    throw new Exception(declaredMethod.getName() + " should have and only have one parameter");
                }
                declaredMethod.invoke(instance, container.getBeanByCompatibleType(parameterTypes[0]));
            }
        }
    }

    private void initSetterInjectionOnField() throws Exception {
        for (Field declaredField : clazz.getDeclaredFields()) {
            if(declaredField.isAnnotationPresent(Inject.class)) {
                injectField(declaredField);
            }
        }
    }

    private void injectField(Field declaredField) throws Exception {
        String name = declaredField.getName();
        Method method = clazz.getMethod("set" + StringUtils.capitalize(name), declaredField.getType());
        method.invoke(instance, container.getBeanByCompatibleType(declaredField.getType()));
    }

    private void initConstructorInjection() throws Exception {
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
            params.add(container.getBeanByCompatibleType(type));
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
