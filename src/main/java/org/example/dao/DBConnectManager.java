package org.example.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class DBConnectManager {

    private static HikariDataSource dataSource;
    private static HikariConfig config;

    static {
        Properties properties = new Properties();

        try (InputStream input = DBConnectManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find database.properties");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database properties", e);
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setConnectionTimeout(50000);
        config.setMaximumPoolSize(100);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
