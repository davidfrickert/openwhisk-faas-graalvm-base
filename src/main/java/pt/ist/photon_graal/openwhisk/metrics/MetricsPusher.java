package pt.ist.photon_graal.openwhisk.metrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.PushGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.photon_graal.openwhisk.conf.MetricsExportConfig;

import java.io.IOException;
import java.util.UUID;

public class MetricsPusher extends Thread {

    private static final Logger log = LoggerFactory.getLogger(MetricsPusher.class);

    private final PushGateway pushGateway;
    private final PrometheusMeterRegistry meterRegistry;

    private static final UUID runtimeIdentifier = UUID.randomUUID();

    public MetricsPusher(final MetricsExportConfig config,
                         final PrometheusMeterRegistry meterRegistry) {
        this.pushGateway = new PushGateway(config.getPushAddress());
        this.meterRegistry = meterRegistry;
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    try {
                        pushGateway.delete(runtimeIdentifier.toString());
                    } catch (IOException e) {
                        log.warn("Error deleting job " + runtimeIdentifier);
                    }
                })
        );
    }

    private void push() {
        try {
            pushGateway.pushAdd(meterRegistry.getPrometheusRegistry(), runtimeIdentifier.toString());
        } catch (IOException e) {
            log.warn("Couldn't push metrics to external system", e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                sleep(50);
                push();
            }
        } catch (InterruptedException e) {
            log.error("Interrupted!", e);
        }
    }
}
