package org.example.DAOTest.RepositoryTest.UtilTest;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.util.EntityExistenceChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

// Устанавливаем последовательное выполнение тестов для одного экземпляра класса
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EntityExistenceCheckerTest {

    @BeforeAll
    public void setUpDatabase() throws SQLException {
        try (Connection connection = DBConnectManager.getConnection();
             Statement statement = connection.createStatement()) {

            // Создаем таблицу, если её ещё нет
            statement.execute("CREATE TABLE IF NOT EXISTS empty_table (id SERIAL PRIMARY KEY)");
        }
    }

    @BeforeEach
    public void cleanUpTable() throws SQLException {
        try (Connection connection = DBConnectManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("TRUNCATE TABLE empty_table");

            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM empty_table");
            int count = 0;
            if (resultSet.next()) {
                 count = resultSet.getInt(1);
            }

            assertEquals(0, count, "Table is not empty after truncating.");
        }
    }

    @Test
    public void testCheckWithEmptyTable() throws SQLException {

        boolean exists = EntityExistenceChecker.isExist("empty_table", 1);
        assertFalse(exists, "Expected no records in the empty table.");
    }

    @Test
    public void testCheckWithExistingRecord() throws SQLException {
        try (Connection connection = DBConnectManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("INSERT INTO empty_table (id) VALUES (1)");
        }

        boolean exists = EntityExistenceChecker.isExist("empty_table", 1);
        assertTrue(exists, "Expected the record with ID 1 to exist.");
    }

    @Test
    public void testCheckNonExistingRecord() throws SQLException {

        boolean exists = EntityExistenceChecker.isExist("empty_table", 2);
        assertFalse(exists, "Expected no record with ID 2 in the table.");
    }
}
