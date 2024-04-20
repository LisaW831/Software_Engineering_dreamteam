package com.napier.sem;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/** Class defined to create the report to query: The population of people, people living in cities, and people not living in cities in each country. */
public class Report_25 {
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

        retrievePopulationStatisticsByCountry(con); // Calls a method to retrieve population statistics by country

        // Close database connection
        a.disconnect(con);
    }

    // Method to retrieve population statistics by country
    public static void retrievePopulationStatisticsByCountry(Connection con) {
        try {
            // Map to store population statistics by country
            Map<String, Long> totalPopulation = new HashMap<>();
            Map<String, Long> populationInCities = new HashMap<>();
            Map<String, Long> populationNotInCities = new HashMap<>();

            // Query to retrieve population statistics for each country
            String sql = "SELECT co.Name AS Country, " +
                    "SUM(co.Population) AS total_population, " +
                    "SUM(ci.Population) AS population_in_cities " +
                    "FROM country co " +
                    "LEFT JOIN city ci ON co.Code = ci.CountryCode " +
                    "GROUP BY co.Name";

            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Populate maps with population statistics by country
            while (resultSet.next()) {
                String country = resultSet.getString("Country");
                long totalPop = resultSet.getLong("total_population");
                long popInCities = resultSet.getLong("population_in_cities");

                totalPopulation.put(country, totalPop);
                populationInCities.put(country, popInCities);
                populationNotInCities.put(country, totalPop - popInCities);
            }

            // Display population statistics by country
            System.out.println("Population Statistics by Country:");
            for (String country : totalPopulation.keySet()) {
                System.out.println("Country: " + country);
                System.out.println("Total Population: " + totalPopulation.get(country));
                System.out.println("Population Living in Cities: " + populationInCities.get(country));
                System.out.println("Population Not Living in Cities: " + populationNotInCities.get(country));
                System.out.println(); // Add a line break for readability
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }
}
