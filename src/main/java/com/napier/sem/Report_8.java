package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

/** Class defined to create the report to query: All the cities in a continent organised by largest population to smallest*/
public class Report_8 {
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

        // Call method to display capital cities of the chosen continent
        displayCitiesOfContinent(con, chosenContinent);

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

    // Method to display cities of a continent by population
    public static void displayCitiesOfContinent(Connection con, String continent) {
        try {
            String sql = "SELECT city.name AS city_name, country.name AS country_name, city.population " +
                    "FROM city " +
                    "JOIN country ON city.countrycode = country.code " +
                    "WHERE country.Continent = ? " +
                    "ORDER BY city.population DESC";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, continent);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("cities in " + continent + " ordered by population (highest to lowest):");
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
