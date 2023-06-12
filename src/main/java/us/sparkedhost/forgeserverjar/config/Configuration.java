package us.sparkedhost.forgeserverjar.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public class Configuration {
    private static final String DEFAULT_VERSION = "unknown";

    private final Properties properties;
    private final File file;

    public Configuration(File file) {
        this.file = file;
        this.properties = new Properties();

        this.reset();
    }

    public String getForgeVersion() {
        return this.properties.getProperty("forgeVersion", DEFAULT_VERSION);
    }

    public void load() throws IOException {
        this.reset();

        if (this.file.exists()) {
            try (InputStream in = Files.newInputStream(this.file.toPath())) {
                this.properties.load(in);
            }
        }
    }

    public void reset() {
        this.properties.clear();
        this.properties.setProperty("forgeVersion", DEFAULT_VERSION);
    }
}
