package com.converter.csvconverter.ArgParser;
import com.converter.csvconverter.CsvConverter.CsvConverter;
import org.apache.commons.cli.*;

import java.util.Collection;
import java.util.Properties;

/**
 * Utility class for parsing command-line arguments and executing corresponding methods.
 *
 * <p>The ArgParser class provides a method, {@code parseArguments}, which takes an array of command-line arguments as input.
 * It defines the available command-line options using the Apache Commons CLI library and parses the provided arguments.
 * Based on the parsed options and values, it calls the desired methods, such as the CSV converter functionality of the CsvConverter class.
 *
 * @see com.converter.csvconverter.CsvConverter.CsvConverter
 * @see org.apache.commons.cli.Options
 * @see org.apache.commons.cli.Option
 * @see org.apache.commons.cli.CommandLineParser
 * @see org.apache.commons.cli.DefaultParser
 * @see org.apache.commons.cli.CommandLine
 */
public class ArgParser {
    /**
     * Parses argument and calls the method based on argument key.
     *
     * @param args Arguments as String.
     */
    public static void parseArguments(String[] args) {
        Options options = new Options();

        // Define your command-line options
        Option csv_option = Option.builder("c")
            .longOpt("csv")
            .desc("Calls CSV converter functionality")
            .hasArg(true)
            .argName("method_name")
            .build();

        Option xlx_option = Option.builder("x")
            .longOpt("xlx")
            .desc("Calls Excel converter functionality")
            .hasArg(true)
            .argName("method_name")
            .build();

        Option sql_option = Option.builder("s")
            .longOpt("sql")
            .desc("Chooses SQL as primary database")
            .hasArg(true)
            .argName("method_name")
            .build();

        Option mdb_option = Option.builder("m")
            .longOpt("mdb")
            .desc("Chooses MongoDB as primary database")
            .hasArg(true)
            .argName("method_name")
            .build();

        options.addOption(csv_option);
        options.addOption(xlx_option);
        options.addOption(sql_option);
        options.addOption(mdb_option);

        CommandLineParser parser = new DefaultParser();
        Properties properties = new Properties();
        try {
            String methodName = null;
            String db_details = null;
            String db_type = null;
            CommandLine cmd = parser.parse(options, args);
            // Based on the parsed method name, execute the desired method
            if (cmd.hasOption('c')) {
                methodName = cmd.getOptionValue("csv");
                if (cmd.hasOption('s')){
                    // CSV + SQL
                    properties.setProperty("spring.profiles.active", "sql");
                    db_details = cmd.getOptionValue("sql");
                    db_type = "sql";
                } else if (cmd.hasOption('m')) {
                    // CSV + MDB
                    System.out.println("MONGODB:" + cmd.getOptionValue("mdb"));
                    properties.setProperty("spring.profiles.active", "mongodb");
                    db_details = cmd.getOptionValue("m");
                    db_type = "mdb";
                }
                else {
                    System.out.println("Missing Database Argument");
                }
            } else if (cmd.hasOption('x')) {
                // to test other option
                System.out.println("other method called" + cmd.getOptionValue("xlx"));

            } else {
                // fails
                System.out.println("Invalid method: ");
            }
            try { CsvConverter.convert(methodName, db_type, db_details);}
            catch (Exception e) { e.printStackTrace(); }
        } catch (ParseException e) {
            System.out.println("Error parsing command-line arguments: " + e.getMessage());
        }
    }
}

