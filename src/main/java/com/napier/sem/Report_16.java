package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

// Class defined to create the report to query: The top N populated cities in a district where N is provided by the user
public class Report_16 {
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
        System.out.print("Enter the number of populated cities in a district to display: ");
        int numberOfCities = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        String districtName = "";
        boolean isValidDistrict = false;
        while (!isValidDistrict) {
            System.out.print("Enter the district: ");
            districtName = scanner.nextLine();
            isValidDistrict = isDistrictValid(con, districtName);
            if (!isValidDistrict) {
                System.out.println("District not found. Please enter a valid district.");
            }
        }

        retrieveCitiesByDistrictPopulation(con, districtName, numberOfCities); // Calls a method to retrieve cities by district population
    }

    // Method to check if the district exists in the city table
    public static boolean isDistrictValid(Connection con, String districtName) {
        try {
            String sql = "SELECT COUNT(*) AS count FROM city WHERE District = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, districtName);
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

    // Method to retrieve cities sorted by population by district
    public static void retrieveCitiesByDistrictPopulation(Connection con, String districtName, int numberOfCities) {
        try {
            String sql = "SELECT Name AS CityName, Population " +
                    "FROM city " +
                    "WHERE District = ? " +
                    "ORDER BY Population DESC LIMIT ?"; // SQL to get the population values by district
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, districtName);
            statement.setInt(2, numberOfCities);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No cities found in the specified district.");
            } else {
                System.out.println("Top " + numberOfCities + " populated cities in " + districtName + ":"); // Indicates the start of the output
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

