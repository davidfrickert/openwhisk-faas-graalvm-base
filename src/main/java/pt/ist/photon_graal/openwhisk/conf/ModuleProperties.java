package pt.ist.photon_graal.openwhisk.conf;

import pt.ist.photon_graal.config.function.Settings;

import java.util.Optional;
import pt.ist.photon_graal.runner.utils.management.IsolateStrategy;
import pt.ist.photon_graal.runner.utils.management.IsolateStrategy.Strategy;

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

    public Optional<IsolateStrategy> getIsolateStrategy() {
        try {
            final Strategy strategy = Strategy.valueOf(config.getProperty("function.isolate.strategy.name"));
            final String[] args = config.getProperty("function.isolate.strategy.args").split(",");

            return Optional.of(IsolateStrategy.fromEnum(strategy, args));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public Settings getFunctionSettings() {
        final Optional<IsolateStrategy> isolateStrategy = getIsolateStrategy();
        return isolateStrategy.map(strategy -> new Settings(getFunctionClassFQN(),
                                                            getFunctionMethod(),
                                                            isFunctionStatic(),
                                                            strategy))
                              .orElseGet(() -> new Settings(getFunctionClassFQN(),
                                                            getFunctionMethod(),
                                                            isFunctionStatic()));
    }
}
