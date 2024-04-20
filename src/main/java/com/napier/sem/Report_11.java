package com.napier.sem;

import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.*;

/** Class defined to create the report to query: All the cities in a district organised by largest population to smallest */

public class Report_11 {
    static Connection con = null;

    public static void main(String[] args) {
        App a = new App();
        Scanner scanner = new Scanner(System.in); // Initialise Scanner for user input

        System.out.println("going in to connect");
        if (args.length < 1) {
            a.connect("localhost:33060", 10000); // Connect to localhost if no arguments provided
        } else {
            a.connect(args[0], Integer.parseInt(args[1])); // Connect to the specified location and port
        }
        con = App.con;

        displayAvailableDistricts(con); // Display available districts to the user

        String chosenDistrict = waitForInput(scanner, "Enter the name of the district you want to explore:", 10);

        // Call method to display cities of the chosen district
        displayCitiesOfDistrict(con, chosenDistrict);

        // Close scanner and database connection
        scanner.close();
        a.disconnect(con);
    }

    // Method to display available districts from the database
    public static void displayAvailableDistricts(Connection con) {
        try {
            String sql = "SELECT DISTINCT District FROM city";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Available Districts:");
            while (resultSet.next()) {
                String district = resultSet.getString("District");
                System.out.println(district); // Print each available District
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Print stack trace if SQL exception occurs
        }
    }

    // Method to display cities of a district
    public static void displayCitiesOfDistrict(Connection con, String district) {
        try {
            String sql = "SELECT city.name AS city_name, city.district AS district_name, city.population " +
                    "FROM city " +
                    "JOIN country ON city.countrycode = country.code " +
                    "WHERE city.district = ? " +
                    "ORDER BY city.population DESC";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, district);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Cities in " + district + " ordered by population (highest to lowest):");
            while (resultSet.next()) {
                String cityName = resultSet.getString("city_name");
                String districtName = resultSet.getString("district_name");
                int population = resultSet.getInt("population");
                System.out.println(cityName + ", " + districtName + " - Population: " + population); // Print city name, district name, and population
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Print stack trace if SQL exception occurs
        }
    }

    // Method to wait for user input within a specified time limit
    public static String waitForInput(Scanner scanner, String prompt, int timeoutSeconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            System.out.println(prompt);
            return scanner.nextLine();
        });

        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            System.out.println("Input timed out. Exiting...");
            System.exit(0); // Exit the program if input times out
        } finally {
            executor.shutdownNow();
        }

        // This line is unreachable but added to satisfy the compiler
        return null;
    }
}
