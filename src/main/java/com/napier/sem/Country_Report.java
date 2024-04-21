package com.napier.sem;

import java.sql.*;

/** Class defined to create the report to query: A country report requires the following columns: Code, Name, Continent, Region, Population, Capital. */
public class Country_Report {
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
        generateCountryReport(con); // Calls a method to generate the country report
        // Close database connection
        a.disconnect(con);
    }

    // Method to generate the country report
    public static void generateCountryReport(Connection con) {
        try {
            // Query to retrieve country information
            String sql = "SELECT Code, Name, Continent, Region, Population, Capital " +
                    "FROM country";
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            // Display the country report
            System.out.println("Country Report:");
            System.out.printf("%-5s %-40s %-20s %-20s %-15s %-5s%n", "Code", "Name", "Continent", "Region", "Population", "Capital");
            while (resultSet.next()) {
                String code = resultSet.getString("Code");
                String name = resultSet.getString("Name");
                String continent = resultSet.getString("Continent");
                String region = resultSet.getString("Region");
                int population = resultSet.getInt("Population");
                int capital = resultSet.getInt("Capital");
                System.out.printf("%-5s %-40s %-20s %-20s %-15s %-5s%n", code, name, continent, region, population, capital);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }
}
