package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

// Class defined to create the report to query: The population of the world, The population of a continent, The population of a region, The population of a country, The population of a district, The population of a city.
public class OverallPopulation {
    static Connection con = null;

    /** This report offers the user to select which population they want to see.
     * Once the result is returned, they are asked if they wish to add a further query or finish */
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
        boolean viewAnotherOption = true;
        while (viewAnotherOption) {
            // Display menu options
            System.out.println("Select an option:");
            System.out.println("1. The population of the world.");
            System.out.println("2. The population of a continent.");
            System.out.println("3. The population of a region.");
            System.out.println("4. The population of a country.");
            System.out.println("5. The population of a district.");
            System.out.println("6. The population of a city.");

            System.out.print("Enter your choice (1-6): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            // Wait for 5 seconds after prompting for user input
            try {
                Thread.sleep(5000); // 5 seconds in milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (choice) {
                case 1:
                    long worldPopulation = retrieveWorldPopulation(con);
                    System.out.println("The population of the world is: " + worldPopulation);
                    break;
                case 2:
                    System.out.print("Enter the continent name: ");
                    String continentName = scanner.nextLine();
                    long continentPopulation = retrieveContinentPopulation(con, continentName);
                    System.out.println("The population of " + continentName + " is: " + continentPopulation);
                    break;
                case 3:
                    System.out.print("Enter the region name: ");
                    String regionName = scanner.nextLine();
                    long regionPopulation = retrieveRegionPopulation(con, regionName);
                    System.out.println("The population of " + regionName + " is: " + regionPopulation);
                    break;
                case 4:
                    System.out.print("Enter the country name: ");
                    String countryName = scanner.nextLine();
                    long countryPopulation = retrieveCountryPopulation(con, countryName);
                    System.out.println("The population of " + countryName + " is: " + countryPopulation);
                    break;
                case 5:
                    System.out.print("Enter the district name: ");
                    String districtName = scanner.nextLine();
                    long districtPopulation = retrieveDistrictPopulation(con, districtName);
                    System.out.println("The population of " + districtName + " is: " + districtPopulation);
                    break;
                case 6:
                    System.out.print("Enter the city name: ");
                    String cityName = scanner.nextLine();
                    long cityPopulation = retrieveCityPopulation(con, cityName);
                    System.out.println("The population of " + cityName + " is: " + cityPopulation);
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number between 1 and 6.");
            }

            // Ask if the user wants to view another option
            System.out.print("Do you want to view another option? (Y/N): ");
            String response = scanner.next();
            if (response.equalsIgnoreCase("N")) {
                viewAnotherOption = false;
            }
        }

        // Close scanner and database connection
        scanner.close();
        a.disconnect(con);
    }

    // Method to retrieve the population of the world
    public static long retrieveWorldPopulation(Connection con) {
        long worldPopulation = 0;
        try {
            String sql = "SELECT SUM(Population) AS WorldPopulation FROM city";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                worldPopulation = resultSet.getLong("WorldPopulation");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return worldPopulation;
    }

    // Method to retrieve the population of a continent
    public static long retrieveContinentPopulation(Connection con, String continentName) {
        long continentPopulation = 0;
        try {
            String sql = "SELECT SUM(Population) AS ContinentPopulation FROM country WHERE Continent = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, continentName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                continentPopulation = resultSet.getLong("ContinentPopulation");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return continentPopulation;
    }

    // Method to retrieve the population of a region
    public static long retrieveRegionPopulation(Connection con, String regionName) {
        long regionPopulation = 0;
        try {
            String sql = "SELECT SUM(Population) AS RegionPopulation FROM country WHERE Region = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, regionName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                regionPopulation = resultSet.getLong("RegionPopulation");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return regionPopulation;
    }

    // Method to retrieve the population of a country
    public static long retrieveCountryPopulation(Connection con, String countryName) {
        long countryPopulation = 0;
        try {
            String sql = "SELECT Population FROM country WHERE Name = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, countryName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                countryPopulation = resultSet.getLong("Population");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return countryPopulation;
    }

    // Method to retrieve the population of a district
    public static long retrieveDistrictPopulation(Connection con, String districtName) {
        long districtPopulation = 0;
        try {
            String sql = "SELECT SUM(Population) AS DistrictPopulation FROM city WHERE District = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, districtName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                districtPopulation = resultSet.getLong("DistrictPopulation");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return districtPopulation;
    }

    // Method to retrieve the population of a city
    public static long retrieveCityPopulation(Connection con, String cityName) {
        long cityPopulation = 0;
        try {
            String sql = "SELECT Population FROM city WHERE Name = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, cityName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                cityPopulation = resultSet.getLong("Population");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cityPopulation;
    }
}
