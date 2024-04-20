package com.napier.sem;

import java.sql.*;

// Class defined to create the report to query: All the capital cities in the world organised by largest population to smallest.
public class Report_17 {
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

    retrieveCapitalsByPopulation(con); // Calls a method to retrieve countries by population
    }

    // Method to retrieve capitals ordered by population
    public static void retrieveCapitalsByPopulation(Connection con) {
        try {
            String sql = "SELECT city.name, city.population, country.name " +
                    "FROM city " +
                    "JOIN country ON city.id = country.capital " +
                    "ORDER BY city.population DESC"; // SQL query to retrieve capitals ordered by population
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Capitals sorted by population (highest to lowest):"); // Indicates the start of the output
            while (resultSet.next()) {
                String cityName = resultSet.getString("name");
                int population = resultSet.getInt("population");
                String countryName = resultSet.getString("country.name");
                System.out.println(cityName + ", " + countryName + " - Population: " + population); // Prints capital name and population
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }
}

