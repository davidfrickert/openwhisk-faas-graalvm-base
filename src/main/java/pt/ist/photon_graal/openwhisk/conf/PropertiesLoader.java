package pt.ist.photon_graal.openwhisk.conf;

import java.io.IOException;
import java.io.InputStream;

public class PropertiesLoader {

    public static TypedProperties load() {
        final TypedProperties config = new TypedProperties();
        try (InputStream fis = PropertiesLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            config.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't initialize application configuration!", e);
        }
        return config;
    }
}
