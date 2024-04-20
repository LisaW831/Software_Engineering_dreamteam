package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

/** Class defined to create the report to query: The top N populated countries in the world where N is provided by the user*/
public class Report_4 {
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
        System.out.print("Enter the number of populated countries in the world to display: ");
        int numberOfCountries = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        Report_4.retrieveCountriesByPopulation(con, numberOfCountries); // Calls a method to retrieve countries by population

        // Close scanner and database connection
        scanner.close();
        a.disconnect(con);
    }

    // Method to retrieve countries sorted by population
    public static void retrieveCountriesByPopulation(Connection con, int numberOfCountries) {
        try {
            String sql = "SELECT Name AS CountryName, Population FROM country ORDER BY Population DESC LIMIT ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, numberOfCountries);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No countries found.");
            } else {
                System.out.println("Top " + numberOfCountries + " populated countries in the world:"); // Indicates the start of the output
                while (resultSet.next()) {
                    String countryName = resultSet.getString("CountryName");
                    int population = resultSet.getInt("Population");
                    System.out.println(countryName + " - Population: " + population); // Prints country name and population
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }
}
