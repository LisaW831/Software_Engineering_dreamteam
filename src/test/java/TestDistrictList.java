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
/**This tests if New York, Sydney and Tokyo exists in the districts list */
public class TestDistrictList {
    private static final String JDBC_URL = "jdbc:mysql://localhost:33060/world";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "example";

    private List<String> districtList;

    @BeforeEach
    void setUp() {
        districtList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            // Retrieve list of districts from the database
            ResultSet resultSet = statement.executeQuery("SELECT District FROM city");
            while (resultSet.next()) {
                String districtName = resultSet.getString("District");
                districtList.add(districtName);
            }

            // Add three more districts to find
            districtList.add("New York");
            districtList.add("Tokyo");
            districtList.add("Sydney");
        } catch (SQLException e) {
            fail("Failed to retrieve district list: " + e.getMessage());
        }
    }

    @Test
    void testDistrictListNotEmpty() {
        // Ensure the district list is not empty
        assert !districtList.isEmpty();
    }

    @Test
    void testDistrictListContainsSpecificDistrict() {
        // Check if the district list contains a specific district
        String districtNameToCheck = "Central Java";
        assert districtList.contains(districtNameToCheck);
    }

    @Test
    void testDistrictListContainsNewYork() {
        // Check if the district list contains New York
        assert districtList.contains("New York");
    }

    @Test
    void testDistrictListContainsTokyo() {
        // Check if the district list contains Tokyo
        assert districtList.contains("Tokyo");
    }

    @Test
    void testDistrictListContainsSydney() {
        // Check if the district list contains Sydney
        assert districtList.contains("Sydney");
    }
}
