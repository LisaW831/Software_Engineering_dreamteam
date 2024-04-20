import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
class MyTest {
    @Test
    void unitTest01() {
        assertEquals(5, 5);
    }

    @Test
    void unitTest3()
    // illustrates how we can add a message to a test
    {
        assertEquals(5, 5, "Messages are equal");
    }

    @Test
    void unitTest4()
    // illustrates how to test floating point values with an error range
    {
        assertEquals(5.0, 5.01, 0.02);
    }

    @Test
    void unitTest5()
    // illustrates how to compare array contents in a test
    {
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};
        assertArrayEquals(a, b);
    }

    @Test
    void unitTest6()
    // illustrates how to test if a value is true
    {
        assertTrue(5 == 5);
    }

    @Test
    void unitTest7()
    // illustrates how to test if a value is false
    {
        assertFalse(5 == 4);
    }

    @Test
    void unitTest8()
    // illustrates how to test if a value is null
    {
        assertNull(null);
    }

    @Test
    void unitTest9()
    // illustrates how to test if a value is not null
    {
        assertNotNull("Hello");
    }

    @Test
    void unitTest10()
    // illustrates how to test if a method throws an exception
    {
        assertThrows(NullPointerException.class, this::throwsException);
    }

    void throwsException() throws NullPointerException {
        throw new NullPointerException();
    }

   @Test
    void generateCountryDetails() {
        // Input
        String countryCode = "NLD";

        // Mock implementation of country details generation
        String[][] countryReport = generateCountryDetails(countryCode);

        // Output verification
        // Verify that the generated country details contains the correct columns
        assertEquals(6, countryReport[0].length); // 6 columns

        assertTrue(arrayContainsValue(countryReport[0], "Code"));
        assertTrue(arrayContainsValue(countryReport[0], "Name"));
        assertTrue(arrayContainsValue(countryReport[0], "Continent"));
        assertTrue(arrayContainsValue(countryReport[0], "Region"));
        assertTrue(arrayContainsValue(countryReport[0], "Population"));
        assertTrue(arrayContainsValue(countryReport[0], "Capital"));
    }

    // Mock implementation of country details generation
    private String[][] generateCountryDetails(String countryCode) {
        return new String[][]{
                {"Code", "Name", "Continent", "Region", "Population", "Capital"},
                {"US", "Netherlands", "Europe", "Western Europe", "15864000", "Amsterdam"}
        };
    }
    // method to check if an array contains a specific value
    private boolean arrayContainsValue(String[] array, String value) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }
    @Test
    void generateCityDetails() {
        // Input
        String cityName = "New York";

        // Mock implementation of city details generation
        String[][] cityReport = generateCityDetails(cityName);

        // Output verification
        // Verify that the generated city details contains the correct columns
        assertEquals(4, cityReport[0].length); // 4 columns

        assertTrue(arrayTestValue(cityReport[0], "Name"));
        assertTrue(arrayTestValue(cityReport[0], "Country"));
        assertTrue(arrayTestValue(cityReport[0], "District"));
        assertTrue(arrayTestValue(cityReport[0], "Population"));
    }

    // Mock implementation of city details generation
    private String[][] generateCityDetails(String cityName) {
        return new String[][] {
                {"Name", "Country", "District", "Population"},
                {"New York", "USA", "New York", "8008278"}
        };
    }

    // method to check if an array contains a specific value
    private boolean arrayTestValue(String[] array, String value) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
