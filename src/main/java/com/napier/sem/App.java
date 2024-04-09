package com.napier.sem;

import java.sql.*;


public class App {
    public static void main(String[] args) {
        App a = new App();
        System.out.println("going in to connect");
        if (args.length < 1) {
            a.connect("localhost:33060", 10000);
        } else {
            a.connect(args[0], Integer.parseInt(args[1]));
        }
        con = App.con;

        retrieveCountriesByPopulation(con);
    }

    public static void retrieveCountriesByPopulation(Connection con) {
        try {
            String sql = "SELECT Name, Population FROM country ORDER BY Population DESC";
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

    static Connection con = null;

    public void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        boolean shouldWait = false;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                if (shouldWait) {
                    // Wait a bit for db to start
                    Thread.sleep(delay);
                }

                // Connect to database
                System.out.println("Going in to  connect");
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());

                // Let's wait before attempting to reconnect
                shouldWait = true;
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

        // Method to establish database connection
        public Connection connect() {
            Connection con = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:33060/world?allowPublicKeyRetrieval=true&useSSL=false";
                con = DriverManager.getConnection(url, "root", "example");
                System.out.println("Connected to the database.");
            } catch (ClassNotFoundException e) {
                System.out.println("Could not load SQL driver: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Failed to connect to the database: " + e.getMessage());
            }
            return con;
        }



        // Method to disconnect from the database
        public void disconnect(Connection con) {
            try {
                if (con != null) {
                    con.close();
                    System.out.println("Disconnected from the database.");
                }
            } catch (SQLException e) {
                System.out.println("Failed to disconnect from the database: " + e.getMessage());
            }
        }
    }

