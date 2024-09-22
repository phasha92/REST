package org.example.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectManager {

    private static final String DRIVER_NAME = "org.postgresql.Driver";

    {
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Sorry, driver not found", e);
        }
    }

    private HikariDataSource dataSource;

    public DBConnectManager() {
        Properties properties = new Properties();
        try (InputStream input = DBConnectManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IllegalArgumentException("Sorry, unable to find db.properties");
            }
            properties.load(input);
            initializeDataSource(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.username"),
                    properties.getProperty("db.password")
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load database properties", e);
        }
    }

    // Конструктор с параметрами(для тестов)
    public DBConnectManager(String url, String user, String password) {
        initializeDataSource(url, user, password);
    }

    // Универсальный метод для инициализации dataSource
    private void initializeDataSource(String url, String user, String password) {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);
            config.setConnectionTimeout(50000);
            config.setMaximumPoolSize(10);
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to initialize connection pool", e);
        }
    }

    // Получение соединения
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource has not been initialized.");
        }
        return dataSource.getConnection();
    }

    // Метод для закрытия пула соединений
    public void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public boolean isClosed() {
        return dataSource.isClosed();
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
