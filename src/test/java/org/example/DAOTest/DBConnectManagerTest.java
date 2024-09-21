package org.example.DAOTest;


import org.example.dao.DBConnectManager;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DBConnectManagerTest {

    private static PostgreSQLContainer<?> postgreSQLContainer;
    private static DBConnectManager dbConnectManager;

    @BeforeAll
    public static void setUp() {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        postgreSQLContainer.start();

        dbConnectManager = new DBConnectManager(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());
    }

    @AfterAll
    public static void tearDown() {
        if (postgreSQLContainer != null) {
            postgreSQLContainer.stop();
        }
    }

    @Test
    public void testGetConnectionAfterInitialization() {
        try (Connection connection = DBConnectManager.getConnection()) {
            assertNotNull(connection, "Connection should not be null");
            assertFalse(connection.isClosed(), "Connection should be open");
        } catch (SQLException e) {
            fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorWithEmptyParams() {
        assertThrows(RuntimeException.class, () -> {
            new DBConnectManager("", "", "");
        }, "Should throw RuntimeException due to empty parameters");
    }

    @Test
    public void testConstructorWithInvalidPassword() {
        assertThrows(RuntimeException.class, () -> {
            new DBConnectManager("jdbc:postgresql://localhost:5432/testdb", "testuser", "invalidpassword");
        }, "Should throw RuntimeException due to invalid password");
    }

    @Test
    public void testConstructorWithInvalidUrl() {
        assertThrows(RuntimeException.class, () -> {
            new DBConnectManager("invalid-url", "testuser", "testpassword");
        }, "Should throw RuntimeException due to invalid URL");
    }


    @Test
    public void testConstructorWithInvalidUser() {
        assertThrows(RuntimeException.class, () -> {
            new DBConnectManager("jdbc:postgresql://localhost:5432/testdb", "invaliduser", "testpassword");
        }, "Should throw RuntimeException due to invalid user");
    }

    @Test
    public void testConstructorWithInvalidParams2() {
        String invalidUrl = "jdbc:postgresql://invalid-host:5432/testdb";
        String user = "testuser";
        String password = "testpassword";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            new DBConnectManager(invalidUrl, user, password);
        });

        String expectedMessage = "Failed to initialize connection pool";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Exception message should contain the expected message");
    }

    // Тест на обработку исключения
    @Test
    public void testConstructorWithInvalidParams() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            new DBConnectManager("invalid-url", "invalid-user", "invalid-password");
        });

        String expectedMessage = "Failed to initialize connection pool";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Expected exception message to contain: " + expectedMessage);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dbConnectManager.getConnection();
        assertNotNull(connection);
        connection.close();  // Закрываем соединение, чтобы не повлиять на другие тесты
    }

    @Test
    public void testMultipleConnections() throws SQLException {
        for (int i = 0; i < 5; i++) {
            Connection connection = dbConnectManager.getConnection();
            assertNotNull(connection);
            connection.close();
        }
    }

    @Test
    public void testInvalidCredentials() {
        assertThrows(RuntimeException.class, () -> {
            new DBConnectManager(
                    postgreSQLContainer.getJdbcUrl(),
                    "invalidUser",
                    "invalidPassword");
        });
    }

    @Test
    public void testInvalidUrl() {
        assertThrows(RuntimeException.class, () -> {
            new DBConnectManager(
                    "jdbc:postgresql://invalid-url:5432/testdb",
                    postgreSQLContainer.getUsername(),
                    postgreSQLContainer.getPassword());
        });
    }
}
