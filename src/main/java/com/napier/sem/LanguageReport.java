package com.napier.sem;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Class defined to create the report to query: a report to provide the number of people who speak the following the following languages from greatest number to smallest, including the percentage of the world population:, Chinese, English, Hindi, Spanish, Arabic.
public class LanguageReport {
    static Connection con = null;

    public static void main(String[] args) {
        App a = new App();
        System.out.println("going in to connect"); // Indicates the start of the connection process
        if (args.length < 1) {
            a.connect("localhost:33060", 10000); // Connects to localhost if no arguments provided
        } else {
            a.connect(args[0], Integer.parseInt(args[1])); // Connects to the specified location and port
        }
        con = App.con;

        retrieveLanguageStatistics(con); // Calls a method to retrieve language statistics

        // Close database connection
        a.disconnect(con);
    }

    // Method to retrieve language statistics
    public static void retrieveLanguageStatistics(Connection con) {
        try {
            // Map to store language population
            Map<String, Long> languagePopulation = new HashMap<>();

            // Query to retrieve total population for each language
            String sql = "SELECT language, SUM(population) AS total_population " +
                    "FROM countrylanguage " +
                    "JOIN country ON countrylanguage.countrycode = country.code " +
                    "GROUP BY language " +
                    "HAVING language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') " +
                    "ORDER BY total_population DESC";

            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            long worldPopulation = getTotalWorldPopulation(con); // Get total world population

            // Populate languagePopulation map and display language statistics
            System.out.println("Language Statistics:");
            while (resultSet.next()) {
                String language = resultSet.getString("language");
                long totalPopulation = resultSet.getLong("total_population");
                double percentage = ((double) totalPopulation / worldPopulation) * 100;
                languagePopulation.put(language, totalPopulation);
                System.out.printf("%s - Population: %d (%.2f%% of world population)\n", language, totalPopulation, percentage);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }

    // Method to retrieve total world population
    public static long getTotalWorldPopulation(Connection con) {
        try {
            String sql = "SELECT SUM(population) AS total_population FROM country";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getLong("total_population");
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
        return 0;
    }
}
