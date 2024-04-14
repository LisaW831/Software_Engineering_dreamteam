package com.napier.sem;

import java.sql.*;
import java.util.Scanner; //Need this to get input from the user

public class Report_12 {
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
        System.out.print("Enter the number of populated cities to display: ");
        int numberOfCities = scanner.nextInt();

        Report_12.retrieveCitiesByPopulation(con, numberOfCities); // Calls a method to retrieve cities by population
    }

    // Method to retrieve cities sorted by population
    public static void retrieveCitiesByPopulation(Connection con, int numberOfCities) {
        try {
            String sql = "SELECT Name, Population FROM city ORDER BY Population DESC LIMIT ?"; // SQL to get the population values
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, numberOfCities);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Top " + numberOfCities + " populated cities in the world:"); // Indicates the start of the output
            while (resultSet.next()) {
                String cityName = resultSet.getString("Name");
                int population = resultSet.getInt("Population");
                System.out.println(cityName + " - Population: " + population); // Prints city name and population
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Prints stack trace if SQL exception occurs
        }
    }
}
