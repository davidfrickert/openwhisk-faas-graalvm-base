package pt.ist.photon_graal.openwhisk.conf;

public interface MetricsExportConfig {
    String getPushHost();
    int getPushPort();
    String getPushAddress();
}
