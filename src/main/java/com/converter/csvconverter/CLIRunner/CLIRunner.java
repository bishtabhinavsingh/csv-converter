package com.converter.csvconverter.CLIRunner;

import com.converter.csvconverter.ArgParser.ArgParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Command-line runner component for parsing and handling command-line arguments.
 *
 * <p>The CLIRunner class is a Spring Boot component that implements the CommandLineRunner interface.
 * It is responsible for receiving and processing command-line arguments passed to the application.
 * The class uses the ArgParser utility class to parse the arguments and handle them accordingly.
 *
 * <p>This class is annotated with the {@code @Component} annotation, making it eligible for component scanning and automatic bean registration
 * within a Spring Boot application context.
 *
 * @see com.converter.csvconverter.ArgParser.ArgParser
 * @see org.springframework.stereotype.Component
 * @see org.springframework.boot.CommandLineRunner
 */
@Component
public class CLIRunner implements CommandLineRunner {
    /**
     * Takes in command line arguments.
     * @param args Arguments as String.
     * @throws Exception
     * @see com.converter.csvconverter.ArgParser.ArgParser
     */
    @Override
    public void run(String... args) throws Exception {
        try {
            ArgParser.parseArguments(args);
        } catch (Exception e) {
            throw new Exception("Parser Failed" + e.getMessage());
        }
    }
}
