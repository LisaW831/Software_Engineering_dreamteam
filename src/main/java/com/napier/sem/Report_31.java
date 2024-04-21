package com.napier.sem;

import java.sql.*;

/** Class defined to create the report to query: All the countries in the world organised by largest population to smallest */
public class Report_31 {
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

        Report_31.retrieveCountriesByPopulation(con); // Calls a method to retrieve countries by population
    }

    // Method to retrieve countries sorted by population
    public static void retrieveCountriesByPopulation(Connection con) {
        try {
            String sql = "SELECT c.Name AS CountryName, c.Population AS TotalPopulation, " +
                    "SUM(ci.Population) AS PopulationInCities, " +
                    "(SUM(ci.Population) / c.Population) * 100 AS PopulationInCitiesPercentage, " +
                    "(1 - (SUM(ci.Population) / c.Population)) * 100 AS PopulationNotInCitiesPercentage " +
                    "FROM country c " +
                    "LEFT JOIN city ci ON c.Code = ci.CountryCode " +
                    "GROUP BY c.Code " +
                    "ORDER BY c.Population DESC"; // SQL query to retrieve countries sorted by population
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Countries sorted by population (highest to lowest):"); // Indicates the start of the output
            while (resultSet.next()) {
                String countryName = resultSet.getString("CountryName");
                int totalPopulation = resultSet.getInt("TotalPopulation");
                int populationInCities = resultSet.getInt("PopulationInCities");
                double populationInCitiesPercentage = resultSet.getDouble("PopulationInCitiesPercentage");
                double populationNotInCitiesPercentage = resultSet.getDouble("PopulationNotInCitiesPercentage");

                System.out.println("Country: " + countryName);
                System.out.println("Total Population: " + totalPopulation);
                System.out.println("Population in Cities: " + populationInCities +
                        " (" + populationInCitiesPercentage + "%)");
                System.out.println("Population Not in Cities: " +
                        (totalPopulation - populationInCities) +
                        " (" + populationNotInCitiesPercentage + "%)");
                System.out.println("--------------------------------------");
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }
}
