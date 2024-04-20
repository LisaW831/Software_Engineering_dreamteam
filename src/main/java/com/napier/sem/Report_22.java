package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

/** Class defined to create the report to query: The top N populated capital cities in a region where N is provided by the user. */
public class Report_22 {
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

        System.out.println("Enter the name of the region you want to explore:");
        String chosenRegion = scanner.nextLine(); // Get user input for region choice

        System.out.println("Enter the number of top populated capital cities you want to retrieve:");
        int topN = scanner.nextInt(); // Get user input for the value of N
        scanner.nextLine(); // Consume the newline character

        // Call method to display top N populated capital cities in the chosen region
        displayTopPopulatedCapitalCities(con, chosenRegion, topN);

        // Close scanner and database connection
        scanner.close();
        a.disconnect(con);
    }

    // Method to display top N populated capital cities in a region
    public static void displayTopPopulatedCapitalCities(Connection con, String region, int topN) {
        try {
            String sql = "SELECT city.name AS city_name, city.population " +
                    "FROM city " +
                    "JOIN country ON city.countrycode = country.code " +
                    "WHERE country.region = ? AND country.capital = city.id " +
                    "ORDER BY city.population DESC " +
                    "LIMIT ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, region);
            preparedStatement.setInt(2, topN);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Top " + topN + " Populated Capital Cities in " + region + ":");
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
