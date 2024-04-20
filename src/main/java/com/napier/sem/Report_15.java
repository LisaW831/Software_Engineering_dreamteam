package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

// Class defined to create the report to query: The top N populated cities in a country where N is provided by the user
public class Report_15 {
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
        System.out.print("Enter the number of populated cities in a country to display: ");
        int numberOfCities = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        String countryName = "";
        boolean isValidCountry = false;
        while (!isValidCountry) {
            System.out.print("Enter the country: ");
            countryName = scanner.nextLine();
            isValidCountry = isCountryValid(con, countryName);
            if (!isValidCountry) {
                System.out.println("Country not found. Please enter a valid country.");
            }
        }

        retrieveCitiesByCountryPopulation(con, countryName, numberOfCities); // Calls a method to retrieve cities by country population
    }

    // Method to check if the country exists in the country table
    public static boolean isCountryValid(Connection con, String countryName) {
        try {
            String sql = "SELECT COUNT(*) AS count FROM country WHERE Name = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, countryName);
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

    // Method to retrieve cities sorted by population by country
    public static void retrieveCitiesByCountryPopulation(Connection con, String countryName, int numberOfCities) {
        try {
            String sql = "SELECT c.Name AS CityName, c.Population " +
                    "FROM city c " +
                    "JOIN country co ON c.CountryCode = co.Code " +
                    "WHERE co.Name = ? " +
                    "ORDER BY c.Population DESC LIMIT ?"; // SQL to get the population values by country
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, countryName);
            statement.setInt(2, numberOfCities);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No cities found in the specified country.");
            } else {
                System.out.println("Top " + numberOfCities + " populated cities in " + countryName + ":"); // Indicates the start of the output
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
