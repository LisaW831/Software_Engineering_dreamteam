package com.napier.sem;

import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.*;

/** Class defined to create the report to query: The top N populated countries in a continent where N is provided by the user*/
public class Report_5 {
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
        System.out.print("Enter the number of populated countries in a continent to display: ");
        int numberOfCountries = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        String continentName = "";
        boolean isValidContinent = false;
        while (!isValidContinent) {
            System.out.print("Enter the continent: ");
            continentName = scanner.nextLine();
            isValidContinent = isContinentValid(con, continentName);
            if (!isValidContinent) {
                System.out.println("Continent not found. Please enter a valid region.");
            }
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            System.out.println("Enter the number of populated countries in a continent to display: ");
            return scanner.nextLine();
        });

        try {
            String continentInput = future.get(10, TimeUnit.SECONDS);
            numberOfCountries = Integer.parseInt(continentInput);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            System.out.println("Input timed out. Using default value for number of countries.");
        } finally {
            executor.shutdownNow();
        }

        retrieveCountriesByContinentPopulation(con, continentName, numberOfCountries); // Calls a method to retrieve countries by region population

        // Close scanner and database connection
        scanner.close();
        a.disconnect(con);
    }

    // Method to check if the continent exists in the country table
    public static boolean isContinentValid(Connection con, String continentName) {
        try {
            String sql = "SELECT COUNT(*) AS count FROM country WHERE Continent = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, continentName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
        return false; // Default to false if an exception occurs
    }

    // Method to retrieve countries sorted by population by continent
    public static void retrieveCountriesByContinentPopulation(Connection con, String continentName, int numberOfCountries) {
        try {
            String sql = "SELECT c.Name AS CountryName, c.Population, co.Name AS ContinentName " +
                    "FROM city c " +
                    "JOIN country co ON c.CountryCode = co.Code " +
                    "WHERE co.Region = ? " +
                    "ORDER BY c.Population DESC LIMIT ?"; // SQL to get the population values by region
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, continentName);
            statement.setInt(2, numberOfCountries);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No cities found in the specified region.");
            } else {
                System.out.println("Top " + numberOfCountries + " populated cities in " + continentName + ":"); // Indicates the start of the output
                while (resultSet.next()) {
                    String countryName = resultSet.getString("countryName");
                    int population = resultSet.getInt("Population");
                    System.out.println(countryName + " - Population: " + population); // Prints city name and population
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }
}
