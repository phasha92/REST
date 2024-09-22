package org.example.dao.repository.util.link;

import org.example.dao.DBConnectManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class LinkActorWithFilmTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private LinkActorWithFilm linkActorWithFilm;
    private DBConnectManager dbConnectManager;

    @BeforeEach
    public void setUp() throws SQLException {
        // Инициализация DBConnectManager с параметрами контейнера PostgreSQL
        dbConnectManager = new DBConnectManager(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());

        // Создание таблиц и вставка тестовых данных
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {

            // Создаем таблицы Actor и Film
            statement.execute("CREATE TABLE IF NOT EXISTS Actor (id SERIAL PRIMARY KEY, name VARCHAR(255));");
            statement.execute("CREATE TABLE IF NOT EXISTS Film (id SERIAL PRIMARY KEY, title VARCHAR(255));");
            statement.execute("CREATE TABLE IF NOT EXISTS Film_Actor (film_id INT, actor_id INT, PRIMARY KEY (film_id, actor_id));");

            // Удаляем старые записи перед вставкой
            statement.execute("DELETE FROM Actor;");
            statement.execute("DELETE FROM Film;");
            statement.execute("DELETE FROM Film_Actor;");

            // Вставляем тестовые данные для актера и фильма
            statement.execute("INSERT INTO Actor (id, name) VALUES (1, 'Actor 1');");
            statement.execute("INSERT INTO Film (id, title) VALUES (1, 'Film 1');");

            // Проверка наличия вставленных данных
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM Actor WHERE id = 1;")) {
                if (!resultSet.next()) {
                    throw new SQLException("Actor with id 1 was not inserted into the database!");
                } else {
                    System.out.println("Actor with id 1 exists.");
                }
            }

            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM Film WHERE id = 1;")) {
                if (!resultSet.next()) {
                    throw new SQLException("Film with id 1 was not inserted into the database!");
                } else {
                    System.out.println("Film with id 1 exists.");
                }
            }
        }

        // Инициализация класса LinkActorWithFilm
        linkActorWithFilm = new LinkActorWithFilm(dbConnectManager);
    }

    @Test
    public void testLinkFilmWithActor_Success() throws SQLException {
        // Тест на успешное связывание фильма с актёром
        linkActorWithFilm.linkFilmWithActor(1, 1);
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Film_Actor WHERE film_id = 1 AND actor_id = 1;")) {
            assertTrue(resultSet.next(), "Film and Actor should be linked.");
        }
    }

    @Test
    public void testLinkFilmWithActor_AlreadyLinked() throws SQLException {
        // Связываем актера с фильмом
        assertDoesNotThrow(() -> linkActorWithFilm.linkFilmWithActor(1, 1));

        // Пытаемся создать ту же связь повторно, ожидая исключение
        SQLException exception = assertThrows(SQLException.class, () -> linkActorWithFilm.linkFilmWithActor(1, 1));
        assertEquals("Entities with ids (1, 1) are already linked", exception.getMessage());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Удаление таблиц после каждого теста
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Film_Actor;");
            statement.execute("DROP TABLE IF EXISTS Actor;");
            statement.execute("DROP TABLE IF EXISTS Film;");
        }
    }
}

