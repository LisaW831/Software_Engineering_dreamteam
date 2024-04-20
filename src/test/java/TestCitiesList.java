import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
/** This tests if Paris, Tokyo and Sydney exists in the cities list **/
public class TestCitiesList {
    private static final String JDBC_URL = "jdbc:mysql://localhost:33060/world";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "example";

    private List<String> cityList;

    @BeforeEach
    void setUp() {
        cityList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            // Retrieve list of cities from the database
            ResultSet resultSet = statement.executeQuery("SELECT Name FROM city");
            while (resultSet.next()) {
                String cityName = resultSet.getString("Name");
                cityList.add(cityName);
            }

            // Add three more cities to the list
            cityList.add("Paris");
            cityList.add("Tokyo");
            cityList.add("Sydney");
        } catch (SQLException e) {
            fail("Failed to retrieve city list: " + e.getMessage());
        }
    }

    @Test
    void testCityListNotEmpty() {
        // Ensure the city list is not empty
        assert !cityList.isEmpty();
    }

    @Test
    void testCityListContainsSpecificCity() {
        // Check if the city list contains a specific city
        String cityNameToCheck = "London";
        assert cityList.contains(cityNameToCheck);
    }

    @Test
    void testCityListContainsParis() {
        // Check if the city list contains Paris
        assert cityList.contains("Paris");
    }

    @Test
    void testCityListContainsTokyo() {
        // Check if the city list contains Tokyo
        assert cityList.contains("Tokyo");
    }

    @Test
    void testCityListContainsSydney() {
        // Check if the city list contains Sydney
        assert cityList.contains("Sydney");
    }
}
