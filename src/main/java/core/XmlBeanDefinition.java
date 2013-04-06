package core;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.AssertUtil.Assert;

public class XmlBeanDefinition extends BeanDefinition {
    private final String id;
    private final List<ParamDesc> consParams;
    private final Map<String, ParamDesc> setterParams;

    public XmlBeanDefinition(String id, Class clazz, String scope, List<ParamDesc> consParams, Map<String, ParamDesc> setterParams) throws Exception {
        super(clazz);
        this.consParams = consParams;
        this.setterParams = setterParams;
        Assert(scope == null || scope.equals("singleton") || scope.equals("prototype"),
                "Bean scope should be either \"signleton\" or \"prototype\"(\"singleton is default is scope attribute not present\").");
        prototypeScope = scope == null ? false : scope.equals("prototype");
        this.id = id;
    }

    @Override
    protected List<Object> determineConsParams(IocContainer container, Constructor constructor) throws Exception {
        ArrayList<Object> values = new ArrayList<Object>();
        Class[] parameterTypes = constructor.getParameterTypes();

        for(int i = 0;i < parameterTypes.length;i++) {
            values.add(consParams.get(i).getValue(parameterTypes[i]));
        }

        return values;
    }

    @Override
    protected Constructor determineConstructor() throws Exception {
        Constructor constructor = findConstructorWithParaNum(consParams.size());
        Assert(constructor != null, "No constructor has " + consParams.size() + "params. Cannot instantiate instance");
        return constructor;
    }

    @Override
    protected void initSetterInjection(IocContainer container, Object instance) throws Exception {
    }

    private Constructor findConstructorWithParaNum(int size) throws Exception {
        Constructor foundOne = null;

        for (Constructor constructor : clazz.getConstructors()) {
            if(constructor.getParameterTypes().length == size) {
                if (foundOne != null) {
                    throw new Exception("More than one constructor has " + size + " params. Cannot decide which one to use");
                }
                foundOne = constructor;
            }
        }
        return foundOne;
    }
}
