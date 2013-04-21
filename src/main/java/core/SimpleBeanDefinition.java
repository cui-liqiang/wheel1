package core;

import com.sun.xml.internal.ws.util.StringUtils;
import core.annotation.Qualified;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SimpleBeanDefinition extends BeanDefinition {

    private List<Field> injectedFields = new ArrayList<Field>();
    private List<Method> injectedSetter = new ArrayList<Method>();

    public SimpleBeanDefinition(Class clazz) throws Exception {
        super(clazz);
        extractInjectField();
        extractInjectSetter();
    }

    protected void extractInjectSetter() throws Exception {
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if(declaredMethod.isAnnotationPresent(Inject.class)) {
                if(declaredMethod.getName().matches("set[A-Z].*]")) {
                    throw new Exception(declaredMethod.getName() + " doesn't look like a setter");
                }
                injectedSetter.add(declaredMethod);
            }
        }
    }

    protected void extractInjectField() {
        for (Field declaredField : clazz.getDeclaredFields()) {
            if(declaredField.isAnnotationPresent(Inject.class)) {
                injectedFields.add(declaredField);
            }
        }
    }

    @Override
    protected List<Object> determineConsParams(IocContainer container, Constructor constructor) throws Exception {
        List<Object> params = new ArrayList<Object>();
        Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();

        int paramPos = 0;
        for (Class type : constructor.getParameterTypes()) {
            params.add(getParam(container, type, parameterAnnotations[paramPos]));
            paramPos++;
        }
        return params;
    }

    private Object getParam(IocContainer container, Class type, Annotation[] parameterAnnotation) throws Exception {
        Qualified qualified = getQualifiedAnnotation(parameterAnnotation);
        return qualified != null ? container.getBeanById(qualified.id())
                : container.getBeanByCompatibleType(type);
    }

    private Qualified getQualifiedAnnotation(Annotation[] parameterAnnotations) {
        for (Annotation parameterAnnotation : parameterAnnotations) {
            if(parameterAnnotation instanceof Qualified)
                return (Qualified)parameterAnnotation;
        }
        return null;
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

            Qualified qualified = setter.getAnnotation(Qualified.class);

            setter.invoke(instance,
                          qualified != null ? container.getBeanById(qualified.id())
                                            : container.getBeanByCompatibleType(parameterTypes[0]));
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

        Qualified qualified = declaredField.getAnnotation(Qualified.class);

        method.invoke(instance,
                      qualified != null ? container.getBeanById(qualified.id())
                                        : container.getBeanByCompatibleType(declaredField.getType()));
    }
}
