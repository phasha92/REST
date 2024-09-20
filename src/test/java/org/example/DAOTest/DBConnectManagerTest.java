package org.example.DAOTest;

import org.example.dao.DBConnectManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
