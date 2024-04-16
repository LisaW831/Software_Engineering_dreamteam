package com.napier.sem;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Report_24 {
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

        retrievePopulationStatisticsByRegion(con); // Calls a method to retrieve population statistics by region

        // Close database connection
        a.disconnect(con);
    }

    // Method to retrieve population statistics by region
    public static void retrievePopulationStatisticsByRegion(Connection con) {
        try {
            // Map to store population statistics by region
            Map<String, Long> totalPopulation = new HashMap<>();
            Map<String, Long> populationInCities = new HashMap<>();
            Map<String, Long> populationNotInCities = new HashMap<>();

            // Query to retrieve population statistics for each region
            String sql = "SELECT co.Region, " +
                    "SUM(co.Population) AS total_population, " +
                    "SUM(ci.Population) AS population_in_cities, " +
                    "(SUM(co.Population) - SUM(ci.Population)) AS population_not_in_cities " +
                    "FROM country co " +
                    "LEFT JOIN city ci ON co.Code = ci.CountryCode " +
                    "GROUP BY co.Region";

            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Populate maps with population statistics by region
            while (resultSet.next()) {
                String region = resultSet.getString("Region");
                long totalPop = resultSet.getLong("total_population");
                long popInCities = resultSet.getLong("population_in_cities");
                long popNotInCities = resultSet.getLong("population_not_in_cities");

                totalPopulation.put(region, totalPop);
                populationInCities.put(region, popInCities);
                populationNotInCities.put(region, popNotInCities);
            }

            // Display population statistics by region
            System.out.println("Population Statistics by Region:");
            for (String region : totalPopulation.keySet()) {
                System.out.println("Region: " + region);
                System.out.println("Total Population: " + totalPopulation.get(region));
                System.out.println("Population Living in Cities: " + populationInCities.get(region));
                System.out.println("Population Not Living in Cities: " + populationNotInCities.get(region));
                System.out.println(); // Add a line break for readability
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }
}
