package pt.ist.photon_graal.openwhisk.conf;

import pt.ist.photon_graal.config.function.Settings;

import java.util.Optional;

public class ModuleProperties implements MetricsExportConfig {

    private final TypedProperties config;

    public ModuleProperties() {
        this.config = PropertiesLoader.load();
    }

    @Override
    public String getPushHost() {
        return config.getProperty("prometheus.push.host", "localhost");
    }

    @Override
    public int getPushPort() {
        return config.getIntOrDefault("prometheus.push.port", 8778);
    }

    @Override
    public String getPushAddress() {
        return getPushHost() + ":" + getPushPort();
    }

    public String getFunctionClassFQN() {
        return config.getProperty("function.class");
    }

    public String getFunctionMethod() {
        return config.getProperty("function.method");
    }

    public boolean isFunctionStatic() {
        return Optional.ofNullable(config.getBoolean("function.static")).orElse(false);
    }

    public Settings getFunctionSettings() {
        return new Settings(getFunctionClassFQN(), getFunctionMethod(), isFunctionStatic());
    }
}
