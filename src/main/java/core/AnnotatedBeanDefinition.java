package core;

import com.sun.xml.internal.ws.util.StringUtils;
import core.scopes.Prototype;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotatedBeanDefinition extends BeanDefinition {
    private Object instance = null;
    private List<Field> injectedFields = new ArrayList<Field>();
    private List<Method> injectedSetter = new ArrayList<Method>();

    public AnnotatedBeanDefinition(Class clazz) throws Exception {
        super(clazz);
        extractMetaData();
    }

    private void extractMetaData() throws Exception {
        id = clazz.getName() + "instance";
        prototypeScope = clazz.isAnnotationPresent(Prototype.class);

        for (Field declaredField : clazz.getDeclaredFields()) {
            if(declaredField.isAnnotationPresent(Inject.class)) {
                injectedFields.add(declaredField);
            }
        }

        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if(declaredMethod.isAnnotationPresent(Inject.class)) {
                if(declaredMethod.getName().matches("set[A-Z].*]")) {
                    throw new Exception(declaredMethod.getName() + " doesn't look like a setter");
                }
                injectedSetter.add(declaredMethod);
            }
        }
    }

    protected Object newInstance(IocContainer container) throws Exception {
        Object instance = initConstructorInjection(container);
        initSetterInjection(container, instance);
        return instance;
    }

    @Override
    protected void initSetterInjection(IocContainer container, Object instance) throws Exception {
        initSetterInjectionOnField(instance, container);
        initSetterInjectionOnSetter(instance, container);
    }

    private void initSetterInjectionOnSetter(Object instance, IocContainer container) throws Exception {
        for (Method setter : injectedSetter) {
            Class<?>[] parameterTypes = setter.getParameterTypes();
            if(parameterTypes.length != 1) {
                throw new Exception(setter.getName() + " should have and only have one parameter");
            }
            setter.invoke(instance, container.getBeanByCompatibleType(parameterTypes[0]));
        }
    }

    private void initSetterInjectionOnField(Object instance, IocContainer container) throws Exception {
        for (Field injectedField : injectedFields) {
            injectField(instance, injectedField, container);
        }
    }

    private void injectField(Object instance, Field declaredField, IocContainer container) throws Exception {
        String name = declaredField.getName();
        Method method = clazz.getMethod("set" + StringUtils.capitalize(name), declaredField.getType());
        method.invoke(instance, container.getBeanByCompatibleType(declaredField.getType()));
    }

    @Override
    protected List<Object> determineConsParams(IocContainer container, Constructor constructor) throws Exception {
        List<Object> params = new ArrayList<Object>();
        for (Class type : constructor.getParameterTypes()) {
            params.add(container.getBeanByCompatibleType(type));
        }
        return params;
    }

    @Override
    protected Constructor determineConstructor() throws Exception {
        Constructor constructor;

        Constructor[] constructors = filterWithInjectAnnotation(clazz.getConstructors());
        if(constructors.length > 1) {
            throw new Exception("classes registered in IOC container must have one or zero constructor, but "
                    + clazz.getName() +
                    " have " + constructors.length);
        } else if(constructors.length == 0) {
            constructor = clazz.getConstructor();
        } else/* if(constructors.length == 1)*/ {
            constructor = constructors[0];
        }
        return constructor;
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
}
