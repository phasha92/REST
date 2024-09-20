package org.example.DAOTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DBConnectManagerTest {

    private static PostgreSQLContainer<?> postgresContainer;

    @BeforeAll
    public static void setUp() {
        // Запускаем тестовый контейнер PostgreSQL
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        postgresContainer.start();

        // Устанавливаем параметры подключения
        System.setProperty("db.url", postgresContainer.getJdbcUrl());
        System.setProperty("db.username", postgresContainer.getUsername());
        System.setProperty("db.password", postgresContainer.getPassword());
    }

    @Test
    public void testGetConnectionWithPropertiesFile() {
        // Проверяем подключение с использованием параметров из файла properties
        DBConnectManager dbManager = new DBConnectManager();
        try (Connection connection = DBConnectManager.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            fail("Connection should be established");
        }
    }

    @Test
    public void testGetConnectionWithConstructorParams() {
        // Проверяем подключение с использованием параметров, переданных через конструктор
        DBConnectManager dbManager = new DBConnectManager(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );
        try (Connection connection = DBConnectManager.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            fail("Connection should be established");
        }
    }

    @Test
    public void testGetConnectionWithInvalidParams() {
        // Проверяем случай с неправильными параметрами подключения
        Exception exception = assertThrows(RuntimeException.class, () -> {
            new DBConnectManager(
                    "jdbc:postgresql://localhost:5432/invalidDB",
                    "wrongUser",
                    "wrongPassword"
            );
        });
        assertTrue(exception.getMessage().contains("Failed to initialize connection pool"));
    }


    @AfterAll
    public static void tearDown() {
        // Останавливаем контейнер после выполнения тестов
        if (postgresContainer != null) {
            postgresContainer.stop();
        }
    }
}

