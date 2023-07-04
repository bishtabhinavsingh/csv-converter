package com.converter.csvconverter.DataBaseHandler;

import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class MongoDBHandlerTest {

    @Test
    void batchInsertBuilder_MongoDB_TestPositive() {

        //Arrange
        MongoDBHandler mongoDBHandler = new MongoDBHandler();
        String csvData3 = "column1,column2,column3\r\n" +
                            "value1,2.5,42\r\n" +
                             "value2,3.4,34";

        CSVReader reader = new CSVReader(new StringReader(csvData3));
//        String expected = "{\"insertMany\": \"null\", \"documents\": [" +
//            "{\"column1\": \"value1\", \"column2\": \"2.5\", \"column3\": \"42\"}, " +
//            "{\"column1\": \"value2\", \"column2\": \"3.4\", \"column3\": \"34\"}" +
//            "]}";
        //String expected = "{\"column1\": \"value2\", \"column2\": \"3.4\", \"column3\": \"34\"}";
        String expected = null;
        try {
            //Act
            String actual = mongoDBHandler.batchInsertBuilder(reader);
            //Assert
            assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
