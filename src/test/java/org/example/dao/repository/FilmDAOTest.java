package org.example.dao.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.example.dao.DBConnectManager;
import org.example.model.Actor;
import org.example.model.Film;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Testcontainers
public class FilmDAOTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private FilmDAO filmDAO;
    private DBConnectManager dbConnectManager;

    @BeforeEach
    public void setUp() throws SQLException {
        dbConnectManager = new DBConnectManager(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());

        // Создаем таблицы Actor и Film
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Actor (id SERIAL PRIMARY KEY, name VARCHAR(255));");
            statement.execute("CREATE TABLE IF NOT EXISTS Film (id SERIAL PRIMARY KEY, title VARCHAR(255), release_year INT);");
            statement.execute("CREATE TABLE IF NOT EXISTS Film_Actor (film_id INT, actor_id INT, PRIMARY KEY (film_id, actor_id));");

            filmDAO = new FilmDAO(dbConnectManager); // Используем новый конструктор
        }
    }

    @Test
    public void testCreateFilm() throws SQLException {
        Film film = new Film();
        film.setTitle("Test Film");
        film.setReleaseYear(2024);
        filmDAO.create(film);

        assertNotNull(film.getId()); // Проверяем, что ID присвоен
        Film retrievedFilm = filmDAO.getById(film.getId());
        assertEquals("Test Film", retrievedFilm.getTitle());
        assertEquals(2024, retrievedFilm.getReleaseYear());
    }

    @Test
    public void testGetById() throws SQLException {
        Film film = new Film();
        film.setTitle("Test Film");
        film.setReleaseYear(2024);
        filmDAO.create(film);

        Film retrievedFilm = filmDAO.getById(film.getId());
        assertNotNull(retrievedFilm);
        assertEquals(film.getTitle(), retrievedFilm.getTitle());
        assertEquals(film.getReleaseYear(), retrievedFilm.getReleaseYear());
    }

    @Test
    public void testUpdateFilm() throws SQLException {
        Film film = new Film();
        film.setTitle("Test Film");
        film.setReleaseYear(2024);
        filmDAO.create(film);

        film.setTitle("Updated Film");
        film.setReleaseYear(2025);
        filmDAO.update(film);

        Film updatedFilm = filmDAO.getById(film.getId());
        assertEquals("Updated Film", updatedFilm.getTitle());
        assertEquals(2025, updatedFilm.getReleaseYear());
    }

    @Test
    public void testDeleteFilm() throws SQLException {
        Film film = new Film();
        film.setTitle("Test Film");
        film.setReleaseYear(2024);
        filmDAO.create(film);

        filmDAO.delete(film.getId());
        Film deletedFilm = filmDAO.getById(film.getId());
        assertNull(deletedFilm); // Проверяем, что фильм удалён
    }

    @Test
    public void testGetAllFilms() throws SQLException {
        Film film1 = new Film();
        film1.setTitle("Film 1");
        film1.setReleaseYear(2020);
        Film film2 = new Film();
        film2.setTitle("Film 2");
        film2.setReleaseYear(2021);

        filmDAO.create(film1);
        filmDAO.create(film2);

        List<Film> films = filmDAO.getAll();
        assertEquals(2, films.size());
    }

    @Test
    public void testGetByIdNonExistentFilmReturnsNull() throws SQLException {
        Film retrievedFilm = filmDAO.getById(999); // Запрос к несуществующему ID
        assertNull(retrievedFilm);
    }

    @Test
    public void testGetActorsByFilmId_WithActors() throws SQLException {
        // Создаем фильм
        Film film = new Film();
        film.setTitle("Test Film");
        film.setReleaseYear(2023);
        filmDAO.create(film);

        // Создаем актёров и связываем их с фильмом
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO Actor (name) VALUES ('Actor 1');");
            statement.execute("INSERT INTO Actor (name) VALUES ('Actor 2');");
            statement.execute("INSERT INTO Film_Actor (film_id, actor_id) VALUES (1, 1);");
            statement.execute("INSERT INTO Film_Actor (film_id, actor_id) VALUES (1, 2);");
        }

        // Проверяем получение актеров
        List<Actor> actors = filmDAO.getActorsByFilmId(film.getId());
        assertEquals(2, actors.size());
        assertEquals("Actor 1", actors.get(0).getName());
        assertEquals("Actor 2", actors.get(1).getName());
    }

    @Test
    public void testGetActorsByFilmId_NoActors() throws SQLException {
        // Создаем фильм без актеров
        Film film = new Film();
        film.setTitle("Test Film No Actors");
        film.setReleaseYear(2023);
        filmDAO.create(film);

        // Проверяем получение актеров
        List<Actor> actors = filmDAO.getActorsByFilmId(film.getId());
        assertTrue(actors.isEmpty()); // Должен вернуть пустой список
    }

    @Test
    public void testGetActorsByFilmId_NonExistentFilm() throws SQLException {
        // Проверяем получение актеров для несуществующего фильма
        List<Actor> actors = filmDAO.getActorsByFilmId(999); // Не существующий ID
        assertTrue(actors.isEmpty()); // Должен вернуть пустой список
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            // Удаляем таблицы после тестов
            statement.execute("DROP TABLE IF EXISTS Film_Actor;");
            statement.execute("DROP TABLE IF EXISTS Film;");
            statement.execute("DROP TABLE IF EXISTS Actor;");
        }
    }
}
