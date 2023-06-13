package com.converter.csvconverter.CsvConverter;
import com.converter.csvconverter.DataBaseHandler.SQLHandler;
import com.opencsv.CSVReader;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvConverterTest {

    @Test
    void isRowValid_SQL_PositiveTest() {
        //Arrange
        SQLHandler sqlhandler = new SQLHandler();
        String csvData1 = "column1,column2,column3\n" +
                            "value1,value2,42\n" +
                            "value3,value4,34";
        CSVReader reader = new CSVReader(new StringReader(csvData1));
        try {
            List<String[]> all_records = reader.readAll();

            //Act
            boolean record1 = sqlhandler.isRowValid(all_records.get(1));
            boolean record2 = sqlhandler.isRowValid(all_records.get(2));

            //Assert
            assertTrue(record1);
            assertTrue(record2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void isRowValid_SQL_NegativeTest() {
        //Arrange
        SQLHandler sqlhandler = new SQLHandler();
        String csvData2 = "column1,column2,column3\n" +
            "value1,,42\n" +
            "value2,value4,";
        CSVReader reader = new CSVReader(new StringReader(csvData2));
        try {
            List<String[]> all_records = reader.readAll();

            //Act
            boolean record1 = sqlhandler.isRowValid(all_records.get(1));
            boolean record2 = sqlhandler.isRowValid(all_records.get(2));

            //Assert
            assertFalse(record1);
            assertFalse(record2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
//    void isRowValid_InConsistentRecord() {
//        //Arrange
//        CsvConverter csvConverter = new CsvConverter();
//        String csvData3 = "column1,column2,column3\n" +
//            "value1,value2,42, extraEntry, extraEntry\n" +
//            "value3,value4,34, extraEntry, extraEntry";
//        Reader reader = new StringReader(csvData3);
//        try {
//            CSVParser par = CSVParser.parse(reader, CSVFormat.DEFAULT);
//
//            //Act
//            List<CSVRecord> records = par.getRecords();
//            boolean record1 = csvConverter.isRowValid(records.get(1));
//            boolean record2 = csvConverter.isRowValid(records.get(2));
//
//            //Assert
//            assertTrue(record1);
//            assertFalse(record2); // Inconsistency check
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    @Test
    void ddlBuilder_SQL_Test(){
        //Arrange
        SQLHandler sqlHandler = new SQLHandler();

        String csvData3 = "column1,column2,column3\r\n" +
            "value1,2.5,42\r\n" +
            "value2,3.4,34";
        CSVReader reader = new CSVReader(new StringReader(csvData3));
        String want = "CREATE TABLE mytable (column1 VARCHAR(255) NOT NULL, column2 DECIMAL NULL, column3 INT NULL, PRIMARY KEY (column1));";
        try {
            //Act
            String actual = sqlHandler.ddlBuilder(reader);

            //Assert
            assertEquals(want, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void batchInsertBuilder_SQL_Test(){
        //Arrange
        String test_csv =  "column1,column2,column3\n" +
            "value1,2.5,42\n" +
            "value2,3.4,34";

        String query_expected = "INSERT INTO mytable (column1, column2, column3)" +
            " VALUES " + "('value1', '2.5', '42')," + "('value2', '3.4', '34');";

        SQLHandler sqlHandler = new SQLHandler();
        //Act
        CSVReader reader = new CSVReader(new StringReader(test_csv));
        try{
            String actual = sqlHandler.batchInsertBuilder(reader);
            //Assert
            assertEquals(query_expected, actual);
        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
