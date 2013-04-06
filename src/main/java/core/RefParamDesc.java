package core;

public class RefParamDesc implements ParamDesc {
    private String ref;

    public RefParamDesc(String ref) {
        this.ref = ref;
    }

    @Override
    public Object getValue(Class parameterType, IocContainer container) throws Exception {
        return container.getBeanById(ref);
    }
}
