package org.example.dao.repository.util;

import org.example.dao.DBConnectManager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Testcontainers
public class EntityExistenceCheckerTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private EntityExistenceChecker checker;
    private DBConnectManager dbConnectManager;

    @BeforeEach
    public void setUp() throws SQLException {
        // Используем конструктор с параметрами для создания DBConnectManager
        dbConnectManager = new DBConnectManager(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());

        // Создаем таблицу и вставляем тестовые данные
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS test_table (id SERIAL PRIMARY KEY);");
            statement.execute("DELETE FROM test_table;"); // Удаляем старые записи перед вставкой

            statement.execute("INSERT INTO test_table (id) VALUES (1);"); // Вставляем одну запись
        }

        checker = new EntityExistenceChecker(dbConnectManager);
    }

    @Test
    public void testIsExist_EntityExists() throws SQLException {
        boolean exists = checker.isExist("test_table", 1);
        assertTrue(exists);
    }

    @Test
    public void testIsExist_EntityDoesNotExist() throws SQLException {
        boolean exists = checker.isExist("test_table", 2);
        assertFalse(exists);
    }

    @Test
    public void testIsExist_NoResultsReturned() throws SQLException {
        // Удаляем все записи
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM test_table;");
        }

        boolean exists = checker.isExist("test_table", 1);
        assertFalse(exists);
    }

    @Test
    public void testEmptyConstructor() {
        EntityExistenceChecker checker = new EntityExistenceChecker();
        // Проверяем, что объект успешно создан
        assertNotNull(checker);
        assertNotNull(checker.getConnectManager());
    }

}
