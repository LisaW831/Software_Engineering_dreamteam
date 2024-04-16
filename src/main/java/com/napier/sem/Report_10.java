package com.napier.sem;

import java.sql.*;
import java.util.Scanner;
public class Report_10 {
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

        displayAvailableCountries(con); // Display available countries to the user

        System.out.println("Enter the name of the country you want to explore:");
        String chosenCountry = scanner.nextLine(); // Get user input for country choice

        // Call method to display cities of the chosen country
        displayCitiesOfCountry(con, chosenCountry);

        // Close scanner and database connection
        scanner.close();
        a.disconnect(con);
    }

    // Method to display available countries from the database
    public static void displayAvailableCountries(Connection con) {
        try {
            String sql = "SELECT DISTINCT Name FROM country";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Available countries:");
            while (resultSet.next()) {
                String countries = resultSet.getString("Name");
                System.out.println(countries); // Print each available Country
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Print stack trace if SQL exception occurs
        }
    }

    // Method to display cities of a country
    public static void displayCitiesOfCountry(Connection con, String country) {
        try {
            String sql = "SELECT city.name AS city_name, country.name AS country_name, city.population " +
                    "FROM city " +
                    "JOIN country ON city.countrycode = country.code " +
                    "WHERE country.Name = ? " +
                    "ORDER BY city.population DESC";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, country);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Cities in " + country + " ordered by population (highest to lowest):");
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
