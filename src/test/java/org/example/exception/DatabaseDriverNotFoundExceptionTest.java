package org.example.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseDriverNotFoundExceptionTest {

    @Test
    void testExceptionMessageAndCause() {
        String expectedMessage = "Driver not found";
        Throwable expectedCause = new ClassNotFoundException("JDBC driver missing");
        DatabaseDriverNotFoundException exception =
                new DatabaseDriverNotFoundException(expectedMessage, expectedCause);
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }

    @Test
    void testThrowingException() {
        String expectedMessage = "Driver initialization failed";
        DatabaseDriverNotFoundException thrown = assertThrows(
                DatabaseDriverNotFoundException.class,
                () -> {
                    throw new DatabaseDriverNotFoundException(expectedMessage, new NullPointerException());
                }
        );
        assertEquals(expectedMessage, thrown.getMessage());
    }
}
