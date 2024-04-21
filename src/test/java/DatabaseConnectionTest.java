import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
/** This is an integration test to test the connection and disconnection to the db */
public class DatabaseConnectionTest {
    private Connection connection;
    private static final String LOCATION = "localhost:33060";
    private static final int DELAY = 500; // milliseconds

    @BeforeEach
    void setUp() {
        // Initialize connection to null before each test
        connection = null;
    }

    @AfterEach
    void tearDown() {
        // Clean up resources after each test
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void testConnect() {
        // Attempt to connect
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + LOCATION
                            + "/world?allowPublicKeyRetrieval=true&useSSL=false",
                    "root", "example");

            // Check if connection is established
            assertNotNull(connection, "Connection should not be null");
            assertTrue(connection.isValid(5), "Connection should be valid");
        } catch (ClassNotFoundException | SQLException e) {
            fail("Failed to connect to the database: " + e.getMessage());
        }
    }

    @Test
    void testDisconnect() {
        // Establish connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + LOCATION
                            + "/world?allowPublicKeyRetrieval=true&useSSL=false",
                    "root", "example");
            assertNotNull(connection, "Connection should not be null");

            // Disconnect and check if connection is closed
            connection.close();
            assertFalse(connection.isValid(5), "Connection should be closed");
        } catch (ClassNotFoundException | SQLException e) {
            fail("Failed to connect to the database: " + e.getMessage());
        }
    }
}
