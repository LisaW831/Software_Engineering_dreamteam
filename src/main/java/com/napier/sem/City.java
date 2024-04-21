package com.napier.sem;

import java.sql.*;

/** Class defined to create the report to query: A city report requires the following columns: Name, Country, District, Population. */
public class City {
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

        generateCapitalCityReport(con); // Calls a method to generate the capital city report

        // Close database connection
        a.disconnect(con);
    }

    // Method to generate the capital city report
    public static void generateCapitalCityReport(Connection con) {
        try {
            // Query to retrieve capital city information
            String sql = "SELECT city.Name AS CityName, country.Name AS CountryName, city.District, city.Population " +
                    "FROM city " +
                    "JOIN country ON city.CountryCode = country.Code " +
                    "WHERE city.ID = country.Capital";

            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Display the capital city report
            System.out.println("Capital City Report:");
            System.out.printf("%-40s %-40s %-20s %-15s%n", "Name", "Country", "District", "Population");
            while (resultSet.next()) {
                String cityName = resultSet.getString("CityName");
                String countryName = resultSet.getString("CountryName");
                String district = resultSet.getString("District");
                int population = resultSet.getInt("Population");

                System.out.printf("%-40s %-40s %-20s %-15s%n", cityName, countryName, district, population);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }
}
