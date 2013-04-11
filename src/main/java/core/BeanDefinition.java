package core;

import com.sun.xml.internal.ws.util.StringUtils;
import core.scopes.Prototype;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class BeanDefinition {
    String id;
    protected Class clazz;
    private Object instance = null;
    protected boolean prototypeScope = false;

    public BeanDefinition(Class clazz) throws Exception {
        this.clazz = clazz;
    }

    public void init(IocContainer container) throws Exception {
        if(isInitialized()) return;
        instance = newInstance(container);
    }

    public boolean assignableTo(Class type) {
        return type.isAssignableFrom(clazz);
    }

    public Object getBean(IocContainer container) throws Exception {
        if(!isInitialized())
            init(container);
        return prototypeScope ? newInstance(container) : instance;
    }

    public boolean matchId(String id) {
        return this.id.equals(id);
    }

    public boolean exactMatchType(Class<?> type) {
        return type.equals(clazz);
    }

    private boolean isInitialized() {
        return instance != null;
    }

    protected Object newInstance(IocContainer container) throws Exception {
        container.startInit(this);
        Object instance = initConstructorInjection(container);
        initSetterInjection(container, instance);
        container.finishInit();
        return instance;
    }

    protected Object initConstructorInjection(IocContainer container) throws Exception {
        Constructor constructor = determineConstructor();
        List<Object> params = determineConsParams(container, constructor);

        return initInstanceWithParams(params, constructor);
    }

    protected abstract List<Object> determineConsParams(IocContainer container, Constructor constructor) throws Exception;

    protected abstract Constructor determineConstructor() throws Exception;

    protected abstract void initSetterInjection(IocContainer container, Object instance) throws Exception;

    private Object initInstanceWithParams(List<Object> params, Constructor constructor) throws Exception {
        switch (params.size()) {
            case 0 : return constructor.newInstance();
            case 1 : return constructor.newInstance(params.get(0));
            case 2 : return constructor.newInstance(params.get(0), params.get(1));
            case 3 : return constructor.newInstance(params.get(0), params.get(1), params.get(2));
            case 4 : return constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3));
            case 5 : return constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4));
            case 6 : return constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5));
            case 7 : return constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5), params.get(6));
            case 8 : return constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5), params.get(6), params.get(7));
            case 9 : return constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5), params.get(6), params.get(7), params.get(8));
            case 10 : return constructor.newInstance(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5), params.get(6), params.get(7), params.get(8), params.get(9));
            default: throw new Exception("too many constructor parameters");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BeanDefinition)) return false;

        BeanDefinition that = (BeanDefinition) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
