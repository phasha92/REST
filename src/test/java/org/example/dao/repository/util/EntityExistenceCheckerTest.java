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
class EntityExistenceCheckerTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private EntityExistenceChecker checker;
    private DBConnectManager dbConnectManager;

    @BeforeEach
    public void setUp() throws SQLException {
        dbConnectManager = new DBConnectManager(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());

        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS test_table (id SERIAL PRIMARY KEY);");
            statement.execute("DELETE FROM test_table;");
            statement.execute("INSERT INTO test_table (id) VALUES (1);");
        }

        checker = new EntityExistenceChecker(dbConnectManager);
    }

    @Test
    void testIsExist_EntityExists() throws SQLException {
        boolean exists = checker.isExist("test_table", 1);
        assertTrue(exists);
    }

    @Test
    void testIsExist_EntityDoesNotExist() throws SQLException {
        boolean exists = checker.isExist("test_table", 2);
        assertFalse(exists);
    }

    @Test
    void testIsExist_NoResultsReturned() throws SQLException {
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM test_table;");
        }

        boolean exists = checker.isExist("test_table", 1);
        assertFalse(exists);
    }

    @Test
    void testEmptyConstructor() {
        EntityExistenceChecker entityExistenceChecker = new EntityExistenceChecker();
        assertNotNull(entityExistenceChecker);
        assertNotNull(entityExistenceChecker.getConnectManager());
    }
}
