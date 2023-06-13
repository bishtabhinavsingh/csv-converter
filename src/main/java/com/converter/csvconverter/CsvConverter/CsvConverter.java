package com.converter.csvconverter.CsvConverter;
import com.converter.csvconverter.DataBaseHandler.DDBHandler;
import com.converter.csvconverter.DataBaseHandler.DataBaseHandler;
import com.converter.csvconverter.DataBaseHandler.SQLHandler;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * Utility class for converting CSV files and sending the data to a database.
 *
 * <p>The CsvConverter class provides methods to convert a CSV file into a CSVReader object,
 * reset an existing CSVReader with a new file, and send the CSV data to a database based on the specified database type and details.
 * It utilizes different handlers for different database types, such as SQL and DynamoDB, to perform the database operations.
 *
 * <p>This class is annotated with the {@code @Component} annotation, making it eligible for component scanning and automatic bean registration
 * within a Spring Boot application context.
 *
 * @see com.converter.csvconverter.DataBaseHandler.DataBaseHandler
 * @see com.converter.csvconverter.DataBaseHandler.SQLHandler
 * @see com.converter.csvconverter.DataBaseHandler.DDBHandler
 * @see com.opencsv.CSVReader
 * @see org.springframework.stereotype.Component
 */
@Component
public class CsvConverter {
    /**
     * Converts CSV file.
     *
     * @param fileName as String.
     * @throws Exception
     * @see java.nio.file.Path
     * @see java.nio.file.Paths
     */
    public static void convert(String fileName, String db_type, String db_details) throws Exception {
        Path filePath = Paths.get(fileName).normalize().toAbsolutePath();
        System.out.println("Getting CSV file from: " + filePath);
        Path db_detailsFilePath = Paths.get(db_details).normalize().toAbsolutePath();
        System.out.println("Getting DB Configuration from from: " + db_detailsFilePath);
        CSVReader processedCSV;
        try {
            processedCSV = processCsv(filePath.toString());

        } catch (Exception e) {
            throw new Exception("CSV Reader error");
        }
        try {
            sendToDB(processedCSV, db_type, db_detailsFilePath.toString(), filePath.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("DB Error");
        }
    }

    /**
     * Converts a CSV file into a CSVReader object.
     *
     * <p>This method takes the file path of a CSV file as a parameter and creates a CSVReader object to read the contents of the file.
     * The CSVReader object allows you to read and parse the CSV data for further processing.
     *
     * @param filePath the file path of the CSV file to be converted
     * @return a CSVReader object representing the CSV file
     * @throws Exception if an error occurs while reading the CSV file or creating the CSVReader object
     */
    private static CSVReader processCsv(String filePath) throws Exception {
        try {
            CSVReader reader =  new CSVReader(new FileReader(filePath));
            System.out.println(reader);
            return reader;
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("CSV Reader error!");
        }
    }

    /**
     * Resets an existing CSVReader with a new file.
     *
     * <p>This method takes an existing CSVReader object and a new file path as parameters.
     * It closes the current CSVReader and creates a new CSVReader object using the provided file path.
     * The new CSVReader can be used to read and parse the contents of the new file.
     *
     * @param reader the existing CSVReader object to be reset
     * @param filePath the file path of the new CSV file
     * @return a new CSVReader object representing the new CSV file
     * @throws IOException if an error occurs while closing the current CSVReader or creating the new CSVReader
     */
    private static CSVReader resetReader(CSVReader reader, String filePath) throws IOException {
        reader.close();
        reader = new CSVReader(new FileReader(filePath));
        return reader;
    }

    /**
     * Sends CSV data to a database based on the specified database type and details.
     *
     * <p>This method takes a CSVReader object, database type, database details, and file path as parameters.
     * It initializes a corresponding DataBaseHandler based on the database type, creates necessary statements for database operations,
     * and sends the CSV data to the database using the DataBaseHandler.
     *
     * @param reader the CSVReader object containing the CSV data
     * @param db_type the type of the database ("sql" for SQL database or "ddb" for DynamoDB)
     * @param db_details the details required for connecting to the database (specific to the database type)
     * @param filePath the file path of the CSV file
     * @throws Exception if an error occurs during the database operations or if the database type is invalid or missing
     */
    private static void sendToDB(CSVReader reader, String db_type, String db_details, String filePath) throws Exception {
        DataBaseHandler db_handler = null;
        switch (db_type){
            case "sql":
                //initialize connection to sql_db
                System.out.println("DB: SQL Selected");
                db_handler = new SQLHandler(db_details);
                System.out.println("DB: SQL Handler Initialized");
                break;

            case "ddb":
                //initialize connection to ddb_db
                System.out.println("DB: DDB Selected");
                db_handler = new DDBHandler();
                break;

            default:
                //db_handler = new SQLHandler();
                System.out.println("Missing / incomplete db arguments");
                break;
        }

        assert db_handler != null;
        String ddl_statement = db_handler.ddlBuilder(reader);
        reader = resetReader(reader, filePath);
        String batchInsert_statement = db_handler.batchInsertBuilder(reader);

        db_handler.createTable(ddl_statement);
        db_handler.uploadToDB(batchInsert_statement);
    }

}
