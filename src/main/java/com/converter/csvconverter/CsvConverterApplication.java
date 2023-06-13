package com.converter.csvconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the CSV Converter application.
 *
 * <p>The CsvConverterApplication class is the main class that bootstraps and runs the CSV Converter application.
 * It uses the Spring Boot framework and provides a command-line interface to convert CSV files into a database.
 *
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 */
@SpringBootApplication
public class CsvConverterApplication {
    /**
     * The main method of the application.
     *
     * <p>This method is responsible for starting the Spring Boot application and running the CSV Converter.
     * It expects the command-line arguments to be provided, and if no arguments are provided, it displays an error message and exits.
     *
     * @param args The command-line arguments passed to the application
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the required argument.");
            System.exit(1); // Exit with a non-zero status code to indicate an error
        }
        SpringApplication.run(CsvConverterApplication.class, args);
    }
}
