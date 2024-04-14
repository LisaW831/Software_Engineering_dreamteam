package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

public class OverallPopulation {
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
        boolean viewAnotherOption = true;
        while (viewAnotherOption) {
            System.out.println("Select an option:");
            System.out.println("1. The population of the world.");
            System.out.println("2. The population of a continent.");
            System.out.println("3. The population of a region.");
            System.out.println("4. The population of a country.");
            System.out.println("5. The population of a district.");
            System.out.println("6. The population of a city.");
            System.out.print("Enter your choice (1-6): ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    int worldPopulation = retrieveWorldPopulation(con);
                    System.out.println("The population of the world is: " + worldPopulation);
                    break;
                case 2:
                    scanner.nextLine(); // Consume newline character
                    System.out.print("Enter the continent name: ");
                    String continentName = scanner.nextLine();
                    int continentPopulation = retrieveContinentPopulation(con, continentName);
                    System.out.println("The population of " + continentName + " is: " + continentPopulation);
                    break;
                case 3:
                    scanner.nextLine(); // Consume newline character
                    System.out.print("Enter the region name: ");
                    String regionName = scanner.nextLine();
                    int regionPopulation = retrieveRegionPopulation(con, regionName);
                    System.out.println("The population of " + regionName + " is: " + regionPopulation);
                    break;
                case 4:
                    scanner.nextLine(); // Consume newline character
                    System.out.print("Enter the country name: ");
                    String countryName = scanner.nextLine();
                    int countryPopulation = retrieveCountryPopulation(con, countryName);
                    System.out.println("The population of " + countryName + " is: " + countryPopulation);
                    break;
                case 5:
                    scanner.nextLine(); // Consume newline character
                    System.out.print("Enter the district name: ");
                    String districtName = scanner.nextLine();
                    int districtPopulation = retrieveDistrictPopulation(con, districtName);
                    System.out.println("The population of " + districtName + " is: " + districtPopulation);
                    break;
                case 6:
                    scanner.nextLine(); // Consume newline character
                    System.out.print("Enter the city name: ");
                    String cityName = scanner.nextLine();
                    int cityPopulation = retrieveCityPopulation(con, cityName);
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
    }

    // Method to retrieve the population of the world
    public static int retrieveWorldPopulation(Connection con) {
        int worldPopulation = 0;
        try {
            String sql = "SELECT SUM(Population) AS WorldPopulation FROM city";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                worldPopulation = resultSet.getInt("WorldPopulation");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return worldPopulation;
    }

    // Method to retrieve the population of a continent
    public static int retrieveContinentPopulation(Connection con, String continentName) {
        int continentPopulation = 0;
        try {
            String sql = "SELECT SUM(Population) AS ContinentPopulation FROM country WHERE Continent = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, continentName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                continentPopulation = resultSet.getInt("ContinentPopulation");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return continentPopulation;
    }

    // Method to retrieve the population of a region
    public static int retrieveRegionPopulation(Connection con, String regionName) {
        int regionPopulation = 0;
        try {
            String sql = "SELECT SUM(Population) AS RegionPopulation FROM country WHERE Region = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, regionName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                regionPopulation = resultSet.getInt("RegionPopulation");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return regionPopulation;
    }

    // Method to retrieve the population of a country
    public static int retrieveCountryPopulation(Connection con, String countryName) {
        int countryPopulation = 0;
        try {
            String sql = "SELECT Population FROM country WHERE Name = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, countryName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                countryPopulation = resultSet.getInt("Population");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return countryPopulation;
    }

    // Method to retrieve the population of a district
    public static int retrieveDistrictPopulation(Connection con, String districtName) {
        int districtPopulation = 0;
        try {
            String sql = "SELECT SUM(Population) AS DistrictPopulation FROM city WHERE District = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, districtName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                districtPopulation = resultSet.getInt("DistrictPopulation");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return districtPopulation;
    }

    // Method to retrieve the population of a city
    public static int retrieveCityPopulation(Connection con, String cityName) {
        int cityPopulation = 0;
        try {
            String sql = "SELECT Population FROM city WHERE Name = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, cityName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                cityPopulation = resultSet.getInt("Population");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cityPopulation;
    }
}
