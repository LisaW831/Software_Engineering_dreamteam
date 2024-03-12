import org.junit.jupiter.api.*;
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

    void throwsException() throws NullPointerException
    {
        throw new NullPointerException();
    }
}
