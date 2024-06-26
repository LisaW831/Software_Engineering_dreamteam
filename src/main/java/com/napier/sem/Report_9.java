package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

/** Class defined to create the report to query: All the cities in a region organised by largest population to smallest */
public class Report_9 {
    static Connection con = null;

    public static void main(String[] args) {
        App a = new App();
        Scanner scanner = new Scanner(System.in); // Initialise Scanner for user input

        System.out.println("going in to connect");
        if (args.length < 1) {
            a.connect("localhost:33060", 10000); // Connect to localhost if no arguments provided
        } else {
            a.connect(args[0], Integer.parseInt(args[1])); // Connect to the specified location and port
        }
        con = App.con;

        displayAvailableRegions(con); // Display available regions to the user

        System.out.println("Enter the name of the region you want to explore:");
        String chosenRegion = scanner.nextLine(); // Get user input for region choice

        // Call method to display cities of the chosen region
        displayCitiesOfRegion(con, chosenRegion);

        // Close scanner and database connection
        scanner.close();
        a.disconnect(con);
    }

    // Method to display available regions from the database
    public static void displayAvailableRegions(Connection con) {
        try {
            String sql = "SELECT DISTINCT Region FROM country";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Available regions:");
            while (resultSet.next()) {
                String countryRegion = resultSet.getString("Region");
                System.out.println(countryRegion); // Print each available region
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Print stack trace if SQL exception occurs
        }
    }

    // Method to display cities of a region by population
    public static void displayCitiesOfRegion(Connection con, String region) {
        try {
            String sql = "SELECT city.name AS city_name, country.name AS country_name, city.population " +
                    "FROM city " +
                    "JOIN country ON city.countrycode = country.code " +
                    "WHERE country.Region = ? " +
                    "ORDER BY city.population DESC";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, region);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Cities in " + region + " ordered by population (highest to lowest):");
            while (resultSet.next()) {
                String cityName = resultSet.getString("city_name");
                String countryName = resultSet.getString("country_name");
                int population = resultSet.getInt("population");
                System.out.println(cityName + ", " + countryName + " - Population: " + population); // Print city name, country name, and population
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Print stack trace if SQL exception occurs
        }
    }
}
