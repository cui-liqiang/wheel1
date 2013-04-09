package core;

public class NullIocContainer extends IocContainer {
    static NullIocContainer instance;

    static {
        try {
            instance = new NullIocContainer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public IocContainer getInstance() {
        return instance;
    }
    NullIocContainer() throws Exception {
        super(null, null, null);
    }

    @Override
    protected Object getUniqueMatchObject(String searchFor, Predicate predicate) throws Exception {
        throw new Exception("Cannot find a bean with " + searchFor + ".Maybe you forget to config it in xml or in @Component?");
    }
}
