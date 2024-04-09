/*package com.napier.sem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//All the countries in the world organised by largest population to smallest.
public class Report_1 {

    public static class CountriesByPopulation {

        private String name;
        private int population;

        public CountriesByPopulation(String name, int population) {
            this.name = name;
            this.population = population;

        }

        // toString method to represent the object as a string
        public String toString() {
            return name +
                    population;
        }
    }

    // Method to retrieve country data for the report
    public static void 2retrieveCountriesByPopulation(Connection con) {
        try {
            String sql = "SELECT Name, Population FROM world.country ORDER BY Population DESC";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Countries sorted by population (highest to lowest):");
            while (resultSet.next()) {
                String countryName = resultSet.getString("Name");
                int population = resultSet.getInt("Population");
                System.out.println(countryName + " - Population: " + population);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

*/