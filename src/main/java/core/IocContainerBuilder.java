package core;

public class IocContainerBuilder {
    private String packageName;
    private String configFile;
    private IocContainer parent;

    public IocContainerBuilder withPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public IocContainerBuilder withConfigFile(String configFile) {
        this.configFile = configFile;
        return this;
    }

    public IocContainer build() throws Exception {
        return new IocContainer(packageName, configFile, parent);
    }

    public IocContainerBuilder withParent(IocContainer parent) {
        this.parent = parent;
        return this;
    }
}
