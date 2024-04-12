package com.napier.sem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class App {

    public static class ClassChooser extends JFrame implements ActionListener {
        private JComboBox<String> classList;
        private JButton runButton;

        // Constructor for the class selection window
        public ClassChooser() {
            setTitle("Class Chooser"); // Set window title
            setDefaultCloseOperation(EXIT_ON_CLOSE); // Set default close operation
            setLayout(new FlowLayout()); // Set layout to FlowLayout

            // List of classes
            String[] classNames = {"Report 1", "Report 2", "Report 3", "Report 17", "Report 18", "Report 19"};
            classList = new JComboBox<>(classNames); // Create JComboBox with class names
            add(classList); // Add JComboBox to the window

            // Run button
            runButton = new JButton("Run"); // Create "Run" button
            runButton.addActionListener(this); // Add action listener to the button
            add(runButton); // Add button to the window

            pack(); // Pack components
            setLocationRelativeTo(null); // Center the window on the screen
            setVisible(true); // Make the window visible
        }

        // Action performed when the button is clicked
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == runButton) {
                String selectedClass = (String) classList.getSelectedItem(); // Get selected class
                switch (selectedClass) {
                    case "Report 1":
                        Report_1.main(new String[]{}); // Run Report 1
                        break;
                    case "Report 2":
                        Report_2.main(new String[]{}); // Run Report 2
                        break;
                    case "Report 3":
                        Report_3.main(new String[]{}); // Run Report 3
                        break;
                    case "Report 17":
                        Report_17.main(new String[]{}); // Run Report 3
                        break;
                    case "Report 18":
                        Report_18.main(new String[]{}); // Run Report 3
                        break;
                    case "Report 19":
                        Report_19.main(new String[]{}); // Run Report 3
                        break;

                    default:
                        System.out.println("Invalid selection"); // Print error message for invalid selection
                }
            }
        }
    }

    public static void main(String[] args) {
        App a = new App();
        System.out.println("going in to connect");
        if (args.length < 1) {
            a.connect("localhost:33060", 10000);
        } else {
            a.connect(args[0], Integer.parseInt(args[1]));
        }
        con = App.con;


            SwingUtilities.invokeLater(() -> new App.ClassChooser()); // Create and display the window

        }

        static Connection con = null;

        public void connect (String location,int delay){
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
        public Connection connect () {
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
        public void disconnect (Connection con){
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



