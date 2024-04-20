package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

// Class defined to create the report to query: The top N populated cities in a continent where N is provided by the user
public class Report_13 {
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
        System.out.print("Enter the number of populated cities by continent to display: ");
        int numberOfCities = scanner.nextInt();

        Report_13.retrieveCitiesByContinentPopulation(con, numberOfCities); // Calls a method to retrieve cities by continent population
    }

    // Method to retrieve cities sorted by population by continent
    public static void retrieveCitiesByContinentPopulation(Connection con, int numberOfCities) {
        try {
            String sql = "SELECT c.Name AS CityName, MAX(c.Population) AS Population, co.Name AS ContinentName " +
                    "FROM city c " +
                    "JOIN country co ON c.CountryCode = co.Code " +
                    "JOIN countrylanguage cl ON co.Code = cl.CountryCode " +
                    "WHERE cl.IsOfficial = true " +
                    "GROUP BY c.Name, co.Name " +  // Include c.Name in the GROUP BY clause
                    "ORDER BY MAX(c.Population) DESC LIMIT ?"; // SQL to get the population values by continent
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, numberOfCities);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Top " + numberOfCities + " populated cities by continent:"); // Indicates the start of the output
            while (resultSet.next()) {
                String cityName = resultSet.getString("CityName");
                int population = resultSet.getInt("Population");
                String continentName = resultSet.getString("ContinentName");
                System.out.println(cityName + " - Population: " + population + " - Continent: " + continentName); // Prints city name, population, and continent
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }
}
