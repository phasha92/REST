package org.example.dao.repository.util;

import org.example.dao.DBConnectManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EntityExistenceChecker {
    public static boolean isExist(String tableName, int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";  // Используем имя таблицы

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;  // Если есть хотя бы одна запись
            }
        }
        return true;
    }
}
