package com.converter.csvconverter.DataBaseHandler;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Configuration
@Profile("mongodb")
public class MongoDBConfig implements DataBaseConfig {

    public MongoDBConfig() {
    }


    @Override
    public DataBaseHandler configDB(String configFile) {
        // Load configuration from YAML file
        Map<String, String> config = DataBaseConfig.loadConfig(configFile);
        System.out.println("DB: MongoDB Config Load SUCCESS");

        // Extract database connection details from the configuration
        String url = config.get("url");
        String DB_NAME = config.get("db_name");
        String DOC_NAME = config.get("collection_name");

        // Establish a connection to MongoDB Cloud
        try  {
            MongoClient mongoClient = MongoClients.create(url);
            // Access the database
            //MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            System.out.println("Connected to MongoDB Cloud successfully.");
            return new MongoDBHandler(mongoClient, DB_NAME, DOC_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
