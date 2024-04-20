import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/** Each test checks that the database contains at least one row before moving to the next **/
public class DatabaseContentTest {
    private static final String JDBC_URL = "jdbc:mysql://localhost:33060/world";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "example";

    @Test
    void testCountryTableContents() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            // Test contents of the country table
            ResultSet resultSet = statement.executeQuery("SELECT * FROM country");
            assertTrue(resultSet.next(), "The table 'country' should contain at least one row");
        } catch (SQLException e) {
            fail("Failed to execute query: " + e.getMessage());
        }
    }

    @Test
    void testCityTableContents() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            // Test contents of the city table
            ResultSet resultSet = statement.executeQuery("SELECT * FROM city");
            assertTrue(resultSet.next(), "The table 'city' should contain at least one row");
        } catch (SQLException e) {
            fail("Failed to execute query: " + e.getMessage());
        }
    }

    @Test
    void testCountryLanguageTableContents() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            // Test contents of the countrylanguage table
            ResultSet resultSet = statement.executeQuery("SELECT * FROM countrylanguage");
            assertTrue(resultSet.next(), "The table 'countrylanguage' should contain at least one row");
        } catch (SQLException e) {
            fail("Failed to execute query: " + e.getMessage());
        }
    }
}
