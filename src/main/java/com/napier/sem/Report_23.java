package com.napier.sem;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Report_23 {
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

        retrievePopulationStatisticsByContinent(con); // Calls a method to retrieve population statistics by continent

        // Close database connection
        a.disconnect(con);
    }

    // Method to retrieve population statistics by continent
    public static void retrievePopulationStatisticsByContinent(Connection con) {
        try {
            // Map to store population statistics by continent
            Map<String, Long> totalPopulation = new HashMap<>();
            Map<String, Long> populationInCities = new HashMap<>();
            Map<String, Long> populationNotInCities = new HashMap<>();

            // Query to retrieve population statistics for each continent
            String sql = "SELECT co.Continent, " +
                    "SUM(co.Population) AS total_population, " +
                    "SUM(ci.Population) AS population_in_cities, " +
                    "(SUM(co.Population) - SUM(ci.Population)) AS population_not_in_cities " +
                    "FROM country co " +
                    "LEFT JOIN city ci ON co.Code = ci.CountryCode " +
                    "GROUP BY co.Continent";

            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Populate maps with population statistics by continent
            while (resultSet.next()) {
                String continent = resultSet.getString("Continent");
                long totalPop = resultSet.getLong("total_population");
                long popInCities = resultSet.getLong("population_in_cities");
                long popNotInCities = resultSet.getLong("population_not_in_cities");

                totalPopulation.put(continent, totalPop);
                populationInCities.put(continent, popInCities);
                populationNotInCities.put(continent, popNotInCities);
            }

            // Display population statistics by continent
            System.out.println("Population Statistics by Continent:");
            for (String continent : totalPopulation.keySet()) {
                System.out.println("Continent: " + continent);
                System.out.println("Total Population: " + totalPopulation.get(continent));
                System.out.println("Population Living in Cities: " + populationInCities.get(continent));
                System.out.println("Population Not Living in Cities: " + populationNotInCities.get(continent));
                System.out.println(); // Add a line break for readability
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }
}
