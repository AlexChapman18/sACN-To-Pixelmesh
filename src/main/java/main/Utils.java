package main;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Utils {

    /**
     * Get the settings from the properties file
     * @return all the properties
     */
    static Properties getSettings() {
        try (InputStream propertiesInput = Files.newInputStream(Paths.get("settings.properties"))) {
            Properties properties = new Properties();
            properties.load(propertiesInput);
            return properties;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
