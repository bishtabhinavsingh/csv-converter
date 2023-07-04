package com.converter.csvconverter;

import com.converter.csvconverter.DataBaseHandler.MongoDBHandler;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.opencsv.CSVReader;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static MongoClient mongoClient;

    public static MongoDBHandler getConnection() throws Exception {
        String url = "mongodb+srv://bishtabhinavsingh:ad1j7OI04dX3wU7k@cluster0.wjp6wtz.mongodb.net/?retryWrites=true&w=majority";
        mongoClient = MongoClients.create(url);
        String csvData3 = "column1,column2,column3\r\n" +
            "value1,2.5,42\r\n" +
            "value2,3.4,34";
        CSVReader reader = new CSVReader(new StringReader(csvData3));
        MongoDBHandler mongoDBHandler = new MongoDBHandler(mongoClient, "db", "collections");
        mongoDBHandler.READER = reader;
        return mongoDBHandler;
    }

    public static void main(String[] args) throws Exception {
        MongoDBHandler mongoDBHandler = getConnection();
        //String batchInsert_statement = mongoDBHandler.batchInsertBuilder(reader);
        //mongoDBHandler.createTable(ddl_statement);
        mongoDBHandler.uploadToDB(" ");
        mongoClient.close();
    }

}
