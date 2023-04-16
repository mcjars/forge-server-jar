package us.sparkedhost;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public class Configuration {
    private static final String DEFAULT_VERSION = "unknown";
    private static final String DEFAULT_CHECK = "true";

    private final Properties properties;
    private final File file;

    public Configuration(File file) {
        this.file = file;
        this.properties = new Properties();

        reset();
    }

    String getForgeVersion() {
        return this.properties.getProperty("forgeVersion", DEFAULT_VERSION);
    }

    String getServerCheck() {
        return this.properties.getProperty("isSparkedCheck", DEFAULT_CHECK);
    }

    void load() throws IOException {
        reset();

        if (this.file.exists()) {
            try (InputStream in = Files.newInputStream(this.file.toPath())) {
                this.properties.load(in);
            }
        }
    }

    void reset() {
        this.properties.clear();
        this.properties.setProperty("forgeVersion", DEFAULT_VERSION);
        this.properties.setProperty("isSparkedCheck", DEFAULT_CHECK);
    }
}
