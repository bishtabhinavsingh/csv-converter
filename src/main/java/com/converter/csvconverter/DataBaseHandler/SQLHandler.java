package com.converter.csvconverter.DataBaseHandler;

import com.converter.csvconverter.Utilities.InferDataType;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * SQL implementation of the DataBaseHandler interface.
 *
 * <p>The SQLHandler class implements the methods defined in the DataBaseHandler interface for handling database operations specific to SQL databases.
 * It provides functionality to build DDL statements, batch insert data, create tables, upload data, and configure the SQL database connection.
 *
 * @see com.converter.csvconverter.DataBaseHandler.DataBaseHandler
 * @see com.opencsv.CSVReader
 * @see org.springframework.stereotype.Component
 * @see org.springframework.context.annotation.ComponentScan
 * @see java.sql.Connection
 * @see java.sql.Statement
 * @see java.sql.SQLException
 * @see org.yaml.snakeyaml.Yaml
 */
@Component
public class SQLHandler implements DataBaseHandler {
    private Connection connection;

    public SQLHandler() {
    }

    public SQLHandler(Connection connection) {
        this.connection = connection;
    }



    /**
     * Checks if current CSV row is a valid row.
     * Valid row is one without any missing values.
     *
     * @param record String[] is a row in CSV file.
     * @return boolean
     *
     */
    public boolean isRowValid(String[] record){
        for (int i = 0; i < record.length; i++){
            if (record[i] == "" || record[i] == null){
                return false;
            }
        }
        return true;
    }

    /**
     * Builds DDL to create a String prepared statement.
     * Looks for headers, iterates through the entire CSV until non-null rows are found.
     * Uses helper function valueInferSQL from InferDataType class to set data types.
     *
     * @param reader CSVReader is a CSV reader.
     * @return String prepared DDL statement.
     * @throws Exception if the input is negative
     * @see InferDataType
     * @see com.opencsv.CSVReader
     *
     */
    @Override
    public String ddlBuilder(CSVReader reader) throws Exception {
        // Read the CSV headers
        String[] headers = reader.readNext();
        String[] row;
        String ddlStatementBuilder = "CREATE TABLE mytable (";
        while (( row = reader.readNext()) != null){
            if (isRowValid(row)){
                for (int i = 0; i < headers.length; i++){
                    String valueType = InferDataType.valueInferSQL(row[i]);
                    String nullable = (i == 0) ? " NOT NULL" : " NULL";
                    ddlStatementBuilder += "" + headers[i] + " " + valueType + nullable + ", ";
                }
                ddlStatementBuilder = ddlStatementBuilder.substring(0, ddlStatementBuilder.length() - 2); // Remove the trailing comma and space
                ddlStatementBuilder += ", PRIMARY KEY (" + headers[0] +"));";
                System.out.println("DB: DDL Statement - " + ddlStatementBuilder);
                return ddlStatementBuilder;
            }
        }
        throw new Exception("Invalid CSV");
    }


    /**
     * Converts CSV file.
     *
     * @param reader is CSVReader format read from CSV file using the OpenCSV.
     * @return String Query for DML
     * @throws Exception
     * @see com.opencsv.CSVReader
     *
     */
    @Override
    public String batchInsertBuilder(CSVReader reader) throws Exception {
        String query = "INSERT INTO mytable (";
        try {
            String[] headers = reader.readNext();
            for (String head: headers){
                System.out.println(head);
                query += head + ", ";
            }
            query = query.substring(0, query.length()-2);
            query += ") VALUES "; // number (?)
            //PreparedStatement ps = this.connection.prepareStatement(query);
            String[] row;
//            while ((row = reader.readerNext() != null)){
//                for ();
//                ps.setObject();
//                ps.addBatch();
//            }

            while ((row = reader.readNext()) != null) {
                String batch = "(";
                System.out.println(row.toString());
                for (String cell : row){
                    batch += "'" + cell + "', ";
                }
                query += batch;
                query = query.substring(0, query.length()-2);
                query += "),";
            }
            query = query.substring(0, query.length()-2);
            query += ");";
            System.out.println("DB: Batch Statement - " + query);
            return query;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    /**
     * Uploads CSV file entries using a String preparedStatement.
     *
     * @param preparedStatement String is CSVReader format read from CSV file using the OpenCSV.
     * @throws SQLException
     * @see java.sql.Connection
     *
     */
    @Override
    public void uploadToDB(String preparedStatement) throws SQLException {
        Statement statement = this.connection.createStatement();
        long n = statement.executeLargeUpdate(preparedStatement);
        statement.close();
        System.out.println("DB: Upload complete, total entries: " + n);
    }

    /**
     * Creates SQL Table using a String DDL.
     *
     * @param ddl String is prepared statement to create SQL table.
     * @throws SQLException
     * @see java.sql.Connection
     *
     */
    @Override
    public void createTable(String ddl) throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.execute(ddl);
        statement.close();
        //connection.close();
        System.out.println("DB: SQL Table created");
    }

}
