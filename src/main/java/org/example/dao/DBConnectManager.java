package org.example.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectManager {

    private static HikariDataSource dataSource;

    // Конструктор с параметрами(для тестов)
    public DBConnectManager(String url, String user, String password) {
        initializeDataSource(url, user, password);
    }

    static {
        Properties properties = new Properties();
        try (InputStream input = DBConnectManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find db.properties");
            }
            properties.load(input);
            initializeDataSource(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.username"),
                    properties.getProperty("db.password")
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    // Универсальный метод для инициализации dataSource
    private static void initializeDataSource(String url, String user, String password) {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);
            config.setConnectionTimeout(50000);
            config.setMaximumPoolSize(30);
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    // Получение соединения
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource has not been initialized.");
        }
        return dataSource.getConnection();
    }
}
