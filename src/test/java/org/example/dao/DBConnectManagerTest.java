package org.example.dao;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;

@Testcontainers
class DBConnectManagerTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private DBConnectManager dbConnectManager;

    @BeforeEach
    public void setUp() {
        dbConnectManager = new DBConnectManager(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());
    }

    @AfterEach
    public void tearDown() {
        dbConnectManager.closeDataSource();
    }

    @Test
    void testGetConnection() throws SQLException {
        Connection connection = dbConnectManager.getConnection();
        Assertions.assertNotNull(connection);
        Assertions.assertFalse(connection.isClosed());
        connection.close();
    }

    @Test
    void testCloseDataSource() {
        dbConnectManager.closeDataSource();
        Assertions.assertTrue(dbConnectManager.isClosed());
    }

    @Test
    void testGetConnectionWithoutInitialization() {
        dbConnectManager.closeDataSource();

        Assertions.assertThrows(SQLException.class, () -> {
            dbConnectManager.getConnection();
        });
    }

    @Test
    void testConstructorInvalidUrl() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            new DBConnectManager("invalid-url", "user", "password");
        });
    }

    @Test
    void testConstructorMissingUrl() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DBConnectManager(null, "user", "password");
        });
    }

    @Test
    void testConstructorInitialization() {
        DBConnectManager manager = new DBConnectManager("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        Assertions.assertNotNull(manager.getDataSource());
        Assertions.assertFalse(manager.isClosed());
    }

    @Test
    void testConstructorMultipleInitializations() {
        DBConnectManager manager1 = new DBConnectManager("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        DBConnectManager manager2 = new DBConnectManager("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        Assertions.assertNotSame(manager1.getDataSource(), manager2.getDataSource());
    }
}
