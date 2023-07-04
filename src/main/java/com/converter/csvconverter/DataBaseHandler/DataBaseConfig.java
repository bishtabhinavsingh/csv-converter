package com.converter.csvconverter.DataBaseHandler;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public interface DataBaseConfig {
    /**
     * Configures SQL based on loaded YAML file.
     * Loads DB url, username, password.
     *
     * @param filePath String is the path to config YAML.
     * @return Map<String, String> that maps the DB url, username, password.
     * @see java.util.Map
     *
     */
    static Map<String, String> loadConfig(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Yaml yaml = new Yaml();
            return yaml.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    DataBaseHandler configDB(String configFile);
}
