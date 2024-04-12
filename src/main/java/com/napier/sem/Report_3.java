package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

public class Report_3 {
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

        // Call method to display countries of the chosen region ordered by population
        displayCountriesOfRegionByPopulation(con, chosenRegion);

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

    // Method to display countries of a region ordered by population
    public static void displayCountriesOfRegionByPopulation(Connection con, String region) {
        try {
            String sql = "SELECT Name, Population FROM country WHERE Region = ? ORDER BY Population DESC";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, region);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Countries in " + region + " ordered by population (highest to lowest):");
            while (resultSet.next()) {
                String countryName = resultSet.getString("Name");
                int population = resultSet.getInt("Population");
                System.out.println(countryName + " - Population: " + population); // Print country name and population
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Print stack trace if SQL exception occurs
        }
    }
}

