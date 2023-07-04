package com.converter.csvconverter.DataBaseHandler;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;


@Configuration
@Profile("sql")
public class SQLConfig implements DataBaseConfig {
    public SQLConfig() {
    }

    /**
     * Configures SQL based on loaded YAML file.
     * Sets DB url, username, password.
     * Uses helper function loadConfig.
     *
     * @param configFile String is the path to config YAML.
     * @see java.util.Map
     * @see java.sql.Connection
     *
     */
    @Override
    public DataBaseHandler configDB(String configFile) {
        // Load configuration from YAML file
        Map<String, String> config = DataBaseConfig.loadConfig(configFile);
        System.out.println("DB: SQL Config Load SUCCESS");

        // Extract database connection details from the configuration
        String url = config.get("url");
        String username = config.get("username");
        String password = config.get("password");

        // Create a database connection
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("DB: SQL Connection SUCCESS");
            return new SQLHandler(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
