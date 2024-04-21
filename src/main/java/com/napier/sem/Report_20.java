package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

/** Class defined to create the report to query: The top N populated capital cities in the world where N is provided by the user. */
public class Report_20 {
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

        System.out.println("Enter the number of top populated capital cities you want to retrieve:");
        int topN = scanner.nextInt(); // Get user input for the value of N
        scanner.nextLine(); // Consume the newline character

        // Call method to display top N populated capital cities in the world
        displayTopPopulatedCapitalCitiesInWorld(con, topN);

        // Close scanner and database connection
        scanner.close();
        a.disconnect(con);
    }

    // Method to display top N populated capital cities in the world
    public static void displayTopPopulatedCapitalCitiesInWorld(Connection con, int topN) {
        try {
            String sql = "SELECT city.name AS city_name, city.population " +
                    "FROM city " +
                    "JOIN country ON city.countrycode = country.code " +
                    "WHERE country.capital = city.id " +
                    "ORDER BY city.population DESC " +
                    "LIMIT ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, topN);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Top " + topN + " Populated Capital Cities in the World:");
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
