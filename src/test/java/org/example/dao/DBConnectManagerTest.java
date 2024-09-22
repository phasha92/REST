package org.example.dao;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectManagerTest {

    private DBConnectManager dbConnectManager;

    @BeforeEach
    public void setUp() {
        // Используем настройки из файла db-test.properties
        dbConnectManager = new DBConnectManager();
    }

    @AfterEach
    public void tearDown() {
        dbConnectManager.closeDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dbConnectManager.getConnection();
        Assertions.assertNotNull(connection);
        Assertions.assertFalse(connection.isClosed());
        connection.close(); // Закрываем соединение после теста
    }

    @Test
    public void testCloseDataSource() {
        dbConnectManager.closeDataSource();
        Assertions.assertTrue(dbConnectManager.isClosed());
    }

    @Test
    public void testInitializationException() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            new DBConnectManager("invalid-url", "user", "password");
        });
    }

    @Test
    public void testGetConnectionWithoutInitialization() {
        dbConnectManager.closeDataSource();

        Assertions.assertThrows(SQLException.class, () -> {
            dbConnectManager.getConnection();
        });
    }

    @Test
    public void testConstructorInvalidUrl() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            new DBConnectManager("invalid-url", "user", "password");
        });
    }

    @Test
    public void testConstructorMissingUrl() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DBConnectManager(null, "user", "password");
        });
    }

    @Test
    public void testConstructorInitialization() {
        DBConnectManager manager = new DBConnectManager("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        Assertions.assertNotNull(manager.getDataSource());
        Assertions.assertFalse(manager.isClosed());
    }

    @Test
    public void testConstructorMultipleInitializations() {
        DBConnectManager manager1 = new DBConnectManager("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        DBConnectManager manager2 = new DBConnectManager("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");

        Assertions.assertNotSame(manager1.getDataSource(), manager2.getDataSource());
    }


}
