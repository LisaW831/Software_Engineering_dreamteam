package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

/** Class defined to create the report to query: The top N populated capital cities in a continent where N is provided by the user. it has a wait for user input*/
public class Report_21 {
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

        System.out.println("Enter the number of top populated cities you want to retrieve:");
        int topN = scanner.nextInt(); // Get user input for the value of N
        scanner.nextLine(); // Consume the newline character

        // Wait for 10 seconds after prompting for user input
        try {
            Thread.sleep(10000); // 10 seconds in milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Call method to display top N populated capital cities in the chosen continent
        displayTopPopulatedCapitalCities(con, chosenContinent, topN);

        // Close scanner and database connection
        scanner.close();
        a.disconnect(con);
    }

    // Method to display available continents from the database
    public static void displayAvailableContinents(Connection con) {
        try {
            String sql = "SELECT DISTINCT continent FROM country";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Available Continents:");
            while (resultSet.next()) {
                String continent = resultSet.getString("continent");
                System.out.println(continent); // Print each available continent
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Print stack trace if SQL exception occurs
        }
    }

    // Method to display top N populated capital cities in a continent
    public static void displayTopPopulatedCapitalCities(Connection con, String continent, int topN) {
        try {
            String sql = "SELECT city.name AS city_name, city.population " +
                    "FROM city " +
                    "JOIN country ON city.countrycode = country.code " +
                    "WHERE country.continent = ? AND country.capital = city.id " +
                    "ORDER BY city.population DESC " +
                    "LIMIT ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, continent);
            preparedStatement.setInt(2, topN);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Top " + topN + " Populated Capital Cities in " + continent + ":");
            int rank = 1;
            while (resultSet.next()) {
                String cityName = resultSet.getString("city_name");
                int population = resultSet.getInt("population");
                System.out.println(rank + ". " + cityName + " - Population: " + population); // Print city name and population
                rank++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Print stack trace if SQL exception occurs
        }
    }
}
