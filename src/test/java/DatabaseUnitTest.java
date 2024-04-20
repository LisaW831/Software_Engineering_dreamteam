import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
/** These tests test the connection, tests contents are in a table and tests it can insert and delete data from a table */
public class DatabaseUnitTest {
    private static final String JDBC_URL = "jdbc:mysql://localhost:33060/world";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "example";
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        connection.setAutoCommit(false); // Disable auto-commit for transaction
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null) {
            connection.rollback(); // Rollback any changes made during the test
            connection.setAutoCommit(true); // Re-enable auto-commit
            connection.close();
        }
    }

    @Test
    void testDatabaseConnection() {
        assertNotNull(connection, "Connection should not be null");
    }

    @Test
    void testTableExistence() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, "country", null);
        assertTrue(resultSet.next(), "Table 'country' should exist");
        resultSet.close();
    }

    @Test
    void testTableStructure() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getColumns(null, null, "country", null);
        boolean hasNameColumn = false;
        boolean hasPopulationColumn = false;
        while (resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");
            if ("Name".equals(columnName)) {
                hasNameColumn = true;
            } else if ("Population".equals(columnName)) {
                hasPopulationColumn = true;
            }
        }
        assertTrue(hasNameColumn, "Table 'country' should have 'Name' column");
        assertTrue(hasPopulationColumn, "Table 'country' should have 'Population' column");
        resultSet.close();
    }

/** This test adds some data to the database to make sure it can, then deletes it so the test can be ran multple times */
    @Test
    void testTransactionCommit() {
        try {
            Statement statement = connection.createStatement();
            // Insert a valid CountryCode first
            statement.executeUpdate("INSERT INTO country (Code, Name, Continent, Region, SurfaceArea, IndepYear, Population, LifeExpectancy, GNP, GNPOld, LocalName, GovernmentForm, HeadOfState, Capital, Code2) VALUES ('ABE', 'TestCountry', 'Asia', 'Southern Asia', 1000.0, 2000, 1000000, 50.0, 1000.0, 800.0, 'TestCountry', 'Republic', 'TestLeader', 1, 'TC')");
            // Now insert a city with the valid CountryCode
            statement.executeUpdate("INSERT INTO city (Name, CountryCode, District, Population) VALUES ('TestCity', 'ABE', 'TestDistrict', 1000)");
            connection.commit(); // Commit the transaction
            // Verify that the city was successfully inserted
            ResultSet resultSet = statement.executeQuery("SELECT * FROM city WHERE Name = 'TestCity'");
            assertTrue(resultSet.next(), "City 'TestCity' should exist in the database");
            resultSet.close();

            // Clean up by deleting the test data
            statement.executeUpdate("DELETE FROM city WHERE Name = 'TestCity'");
            statement.executeUpdate("DELETE FROM country WHERE Code = 'ABE'");
            connection.commit(); // Commit the deletion

            // Verify that the test data has been successfully deleted
            resultSet = statement.executeQuery("SELECT * FROM city WHERE Name = 'TestCity'");
            assertFalse(resultSet.next(), "City 'TestCity' should have been deleted");
            resultSet.close();

            resultSet = statement.executeQuery("SELECT * FROM country WHERE Code = 'ABE'");
            assertFalse(resultSet.next(), "Country with Code 'ABE' should have been deleted");
            resultSet.close();
        } catch (SQLException e) {
            fail("Transaction failed: " + e.getMessage());
        }
    }
}
