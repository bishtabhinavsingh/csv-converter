package com.converter.csvconverter.DataBaseHandler;

import com.opencsv.CSVReader;

/**
 * DynamoDB implementation of the DataBaseHandler interface (WORK IN PROGRESS).
 *
 * <p>The DDBHandler class implements the methods defined in the DataBaseHandler interface for handling database operations specific to DynamoDB.
 * It provides functionality to build DDL statements, batch insert data, create tables, upload data, and configure the DynamoDB connection.
 *
 * @see com.converter.csvconverter.DataBaseHandler.DataBaseHandler
 * @see com.opencsv.CSVReader
 */
public class DDBHandler implements DataBaseHandler {

    public String ddlBuilder(CSVReader reader) throws Exception{
        try {
            System.out.println("Upload Complete");
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("DDL Build Failed");
        }
        return "";
    }
    public String batchInsertBuilder(CSVReader reader) throws Exception {
        try {
            System.out.println("Upload Complete");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Insert Builder Failed");
        }
        return "";
    }

    @Override
    public void createTable(String ddl) {

    }

    @Override
    public void uploadToDB(String ps) {
        System.out.println("Upload Complete");
    }

    @Override
    public void configDB(String config) {

    }
}
