package pt.ist.photon_graal.openwhisk.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

public class MetricsConstants {
    private static MeterRegistry meterRegistry;

    public static synchronized MeterRegistry get() {
        if (meterRegistry == null) {
            meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        }
        return meterRegistry;
    }
}
