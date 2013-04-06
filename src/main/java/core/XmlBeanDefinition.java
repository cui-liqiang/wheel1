package core;

import com.sun.xml.internal.ws.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.AssertUtil.Assert;

public class XmlBeanDefinition extends BeanDefinition {
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
            values.add(consParams.get(i).getValue(parameterTypes[i], container));
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
        for (Map.Entry<String, ParamDesc> entry : setterParams.entrySet()) {
            String name = entry.getKey();
            ParamDesc refId = entry.getValue();

            String methodName = "set" + StringUtils.capitalize(name);
            Method method = filterMethodByName(clazz.getMethods(), methodName);
            Assert(method != null, "Cannot find method \"" + methodName + "\" in class " + clazz.getName() + "for injection");

            method.invoke(instance, refId.getValue(method.getParameterTypes()[0], container));
        }
    }

    private Method filterMethodByName(Method[] methods, String methodName) throws Exception {
        Method foundMethod = null;
        for (Method method : methods) {
            if(method.getName().equals(methodName) && method.getParameterTypes().length == 1) {
                if(foundMethod != null) {
                    throw new Exception("Found more than one \"" + methodName + "\" method with one parameter in class "
                            + clazz.getName() +". Cannot determine which one to use for injection");
                }
                foundMethod = method;
            }
        }
        return foundMethod;
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
