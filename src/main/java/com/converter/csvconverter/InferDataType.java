package com.converter.csvconverter;

import java.math.BigDecimal;

/**
 * Utility class for inferring data types from string values.
 *
 * <p>The InferDataType class provides methods to infer the data type of a given string value.
 * It includes methods to check if a value is numeric or decimal, and provides a default data type of VARCHAR(255) if no specific type can be inferred.
 *
 * @see java.math.BigDecimal
 */
public class InferDataType {

    /**
     * Checks if a string value represents a numeric value.
     *
     * @param value The string value to check
     * @return true if the value is numeric, false otherwise
     */
    public boolean isNumeric(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a string value represents a decimal value.
     *
     * @param value The string value to check
     * @return true if the value is a decimal, false otherwise
     */
    public boolean isDecimal(String value) {
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

//    public static boolean isDate(String value) {
//        // Implement logic to validate if the value is a valid date
//        // Return true if it is, false otherwise
//    }

    /**
     * Infers the SQL data type for a given value.
     *
     * @param unknown The value for which to infer the data type
     * @return the inferred SQL data type as a string
     * @see java.math.BigDecimal
     */
    public static String valueInferSQL(String unknown){
        InferDataType inferDataType = new InferDataType();
        if (inferDataType.isNumeric(unknown)){
            return "INT";
        } else if (inferDataType.isDecimal(unknown)){
            return "DECIMAL";
        }
        return "VARCHAR(255)";
    }
}
