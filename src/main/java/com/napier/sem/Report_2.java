package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

// Class defined to create the report to query: All the countries in a continent organised by largest population to smallest
public class Report_2 {
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

        displayAvailableContinents(con); // Display available continents to the user

        System.out.println("Enter the name of the continent you want to explore:");
        String chosenContinent = scanner.nextLine(); // Get user input for continent choice

        // Call method to display countries of the chosen continent ordered by population
        displayCountriesOfContinentByPopulation(con, chosenContinent);

        // Close scanner and database connection
        scanner.close();
        a.disconnect(con);
    }

    // Method to display available continents from the database
    public static void displayAvailableContinents(Connection con) {
        try {
            String sql = "SELECT DISTINCT Continent FROM country";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Available continents:");
            while (resultSet.next()) {
                String countryContinent = resultSet.getString("Continent");
                System.out.println(countryContinent); // Print each available continent
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Print stack trace if SQL exception occurs
        }
    }

    // Method to display countries of a continent ordered by population
    public static void displayCountriesOfContinentByPopulation(Connection con, String continent) {
        try {
            String sql = "SELECT Name, Population FROM country WHERE Continent = ? ORDER BY Population DESC";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, continent);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Countries in " + continent + " ordered by population (highest to lowest):");
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