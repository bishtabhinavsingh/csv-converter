package com.converter.csvconverter.DataBaseHandler;

import com.opencsv.CSVReader;
import java.sql.SQLException;

/**
 * Interface for handling database operations and configurations.
 *
 * <p>The DataBaseHandler interface defines the contract for classes that handle database operations, such as creating tables,
 * uploading data, and configuring the database connection.
 * Classes implementing this interface are responsible for implementing these methods to perform the specific database operations.
 *
 * @see com.opencsv.CSVReader
 * @see java.sql.SQLException
 */
public interface DataBaseHandler {


    String ddlBuilder(CSVReader reader) throws Exception;
    String batchInsertBuilder(CSVReader reader) throws Exception;
    void createTable(String ddl) throws Exception;
    void uploadToDB(String ps) throws Exception;

}
