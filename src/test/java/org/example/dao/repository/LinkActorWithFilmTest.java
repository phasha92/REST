package org.example.dao.repository;

import org.example.dao.DBConnectManager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;

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
        dbConnectManager = new DBConnectManager(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());

        // Создаем таблицы Film, Actor и Film_Actor
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Film (id SERIAL PRIMARY KEY);");
            statement.execute("CREATE TABLE IF NOT EXISTS Actor (id SERIAL PRIMARY KEY);");
            statement.execute("CREATE TABLE IF NOT EXISTS Film_Actor (film_id INT, actor_id INT, PRIMARY KEY (film_id, actor_id));");

            // Вставляем тестовые данные
            statement.execute("INSERT INTO Film (id) VALUES (1);");
            statement.execute("INSERT INTO Actor (id) VALUES (1);");
        }
        linkActorWithFilm = new LinkActorWithFilm(dbConnectManager);
    }

    @Test
    public void testLinkEntities_Success() throws SQLException {
        linkActorWithFilm.linkEntities(1, 1); // Связываем фильм и актёра
        // Проверяем, что связь была успешно создана
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Film_Actor WHERE film_id = 1 AND actor_id = 1;")) {
            assertTrue(resultSet.next()); // Связь должна существовать
        }
    }

    @Test
    public void testLinkEntities_AlreadyLinked() {
        // Сначала создаем связь
        assertDoesNotThrow(() -> linkActorWithFilm.linkEntities(1, 1));

        // Теперь пытаемся создать её снова
        SQLException exception = assertThrows(SQLException.class, () -> linkActorWithFilm.linkEntities(1, 1));
        assertEquals("Film id(1) and actor id(1) are already linked", exception.getMessage());
    }

    @Test
    public void testLinkFilmWithActor_FilmDoesNotExist() {
        // Проверяем, что выбрасывается исключение при попытке привязать несуществующий фильм
        SQLException exception = assertThrows(SQLException.class, () -> linkActorWithFilm.linkFilmWithActor(2222, 1));
        assertEquals("Film with id 2222 does not exist.", exception.getMessage());
    }

    @Test
    public void testLinkFilmWithActor_ActorDoesNotExist() {
        // Проверяем, что выбрасывается исключение при попытке привязать актёра, который не существует
        SQLException exception = assertThrows(SQLException.class, () -> linkActorWithFilm.linkFilmWithActor(1, 2222));

        // Проверяем сообщение исключения
        assertEquals("Actor with id 2222 does not exist.", exception.getMessage());
    }



    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            // Удаляем таблицы после тестов
            statement.execute("DROP TABLE IF EXISTS Film_Actor;");
            statement.execute("DROP TABLE IF EXISTS Actor;");
            statement.execute("DROP TABLE IF EXISTS Film;");
        }
    }
}
