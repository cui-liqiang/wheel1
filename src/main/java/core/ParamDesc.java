package core;

public interface ParamDesc {
    public Object getValue(Class parameterType, IocContainer container) throws Exception;
}
