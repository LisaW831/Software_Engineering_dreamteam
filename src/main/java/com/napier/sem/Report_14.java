package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

// Class defined to create the report to query: The top N populated cities in a region where N is provided by the user
public class Report_14 {
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

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of populated cities in a region to display: ");
        int numberOfCities = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        String regionName = "";
        boolean isValidRegion = false;
        while (!isValidRegion) {
            System.out.print("Enter the region: ");
            regionName = scanner.nextLine();
            isValidRegion = isRegionValid(con, regionName);
            if (!isValidRegion) {
                System.out.println("Region not found. Please enter a valid region.");
            }
        }

        Report_14.retrieveCitiesByRegionPopulation(con, regionName, numberOfCities); // Calls a method to retrieve cities by region population
    }

    // Method to check if the region exists in the country table
    public static boolean isRegionValid(Connection con, String regionName) {
        try {
            String sql = "SELECT COUNT(*) AS count FROM country WHERE Region = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, regionName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
        return false; // Default to false if an exception occurs
    }

    // Method to retrieve cities sorted by population by region
    public static void retrieveCitiesByRegionPopulation(Connection con, String regionName, int numberOfCities) {
        try {
            String sql = "SELECT c.Name AS CityName, c.Population, co.Name AS RegionName " +
                    "FROM city c " +
                    "JOIN country co ON c.CountryCode = co.Code " +
                    "WHERE co.Region = ? " +
                    "ORDER BY c.Population DESC LIMIT ?"; // SQL to get the population values by region
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, regionName);
            statement.setInt(2, numberOfCities);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No cities found in the specified region.");
            } else {
                System.out.println("Top " + numberOfCities + " populated cities in " + regionName + ":"); // Indicates the start of the output
                while (resultSet.next()) {
                    String cityName = resultSet.getString("CityName");
                    int population = resultSet.getInt("Population");
                    System.out.println(cityName + " - Population: " + population); // Prints city name and population
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }
}
