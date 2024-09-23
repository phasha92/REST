package org.example.dao.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.example.dao.DBConnectManager;
import org.example.model.Actor;
import org.example.model.Director;
import org.example.model.Film;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
class FilmDAOTest {

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

        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Director (id SERIAL PRIMARY KEY, name VARCHAR(255));");
            statement.execute("CREATE TABLE IF NOT EXISTS Actor (id SERIAL PRIMARY KEY, name VARCHAR(255));");
            statement.execute("CREATE TABLE IF NOT EXISTS Film (id SERIAL PRIMARY KEY, title VARCHAR(255), release_year INT, director_id INT);");
            statement.execute("CREATE TABLE IF NOT EXISTS Film_Actor (film_id INT, actor_id INT, PRIMARY KEY (film_id, actor_id));");
            filmDAO = new FilmDAO(dbConnectManager);
        }
    }

    @Test
    void testCreateFilm() throws SQLException {
        Film film = new Film();
        film.setTitle("Test Film");
        film.setReleaseYear(2024);
        film.setDirector(new Director(1, "Director Name", new ArrayList<>()));
        filmDAO.create(film);
        Film retrievedFilm = filmDAO.getById(film.getId());
        assertEquals("Test Film", retrievedFilm.getTitle());
        assertEquals(2024, retrievedFilm.getReleaseYear());
    }

    @Test
    void testGetById() throws SQLException {
        Film film = new Film();
        film.setTitle("Test Film");
        film.setReleaseYear(2024);
        film.setDirector(new Director(1, "Director Name", new ArrayList<>()));
        filmDAO.create(film);
        Film retrievedFilm = filmDAO.getById(film.getId());
        assertNotNull(retrievedFilm);
        assertEquals(film.getTitle(), retrievedFilm.getTitle());
        assertEquals(film.getReleaseYear(), retrievedFilm.getReleaseYear());
    }

    @Test
    void testUpdateFilm() throws SQLException {
        Film film = new Film();
        film.setTitle("Test Film");
        film.setReleaseYear(2024);
        film.setDirector(new Director(1, "Director Name", new ArrayList<>()));
        filmDAO.create(film);
        film.setTitle("Updated Film");
        film.setReleaseYear(2025);
        filmDAO.update(film);
        Film updatedFilm = filmDAO.getById(film.getId());
        assertEquals("Updated Film", updatedFilm.getTitle());
        assertEquals(2025, updatedFilm.getReleaseYear());
    }

    @Test
    void testDeleteFilm() throws SQLException {
        Film film = new Film();
        film.setTitle("Test Film");
        film.setReleaseYear(2024);
        film.setDirector(new Director(1, "Director Name", new ArrayList<>()));
        filmDAO.create(film);
        filmDAO.delete(film.getId());
        Film deletedFilm = filmDAO.getById(film.getId());
        assertNull(deletedFilm);
    }

    @Test
    void testGetAllFilms() throws SQLException {
        Film film1 = new Film();
        film1.setTitle("Film 1");
        film1.setReleaseYear(2020);
        film1.setDirector(new Director(1, "Director Name", new ArrayList<>()));
        Film film2 = new Film();
        film2.setTitle("Film 2");
        film2.setReleaseYear(2021);
        film2.setDirector(new Director(1, "Director Name", new ArrayList<>()));
        filmDAO.create(film1);
        filmDAO.create(film2);
        List<Film> films = filmDAO.getAll();
        assertEquals(2, films.size());
    }

    @Test
    void testGetByIdNonExistentFilmReturnsNull() throws SQLException {
        Film retrievedFilm = filmDAO.getById(999);
        assertNull(retrievedFilm);
    }

    @Test
    void testGetActorsByFilmId_WithActors() throws SQLException {
        Film film = new Film();
        film.setTitle("Test Film");
        film.setReleaseYear(2023);
        film.setDirector(new Director(1, "Director Name", new ArrayList<>()));
        filmDAO.create(film);
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO Actor (name) VALUES ('Actor 1');");
            statement.execute("INSERT INTO Actor (name) VALUES ('Actor 2');");
            statement.execute("INSERT INTO Film_Actor (film_id, actor_id) VALUES (" + film.getId() + ", 1);");
            statement.execute("INSERT INTO Film_Actor (film_id, actor_id) VALUES (" + film.getId() + ", 2);");
        }

        List<Actor> actors = filmDAO.getActorsByFilmId(film.getId());
        assertEquals(2, actors.size());
        assertEquals("Actor 1", actors.get(0).getName());
        assertEquals("Actor 2", actors.get(1).getName());
    }

    @Test
    void testGetActorsByFilmId_NoActors() throws SQLException {
        Film film = new Film();
        film.setTitle("Test Film No Actors");
        film.setReleaseYear(2023);
        film.setDirector(new Director(1, "Director Name", new ArrayList<>()));
        filmDAO.create(film);
        List<Actor> actors = filmDAO.getActorsByFilmId(film.getId());
        assertTrue(actors.isEmpty());
    }

    @Test
    void testGetActorsByFilmId_NonExistentFilm() throws SQLException {
        List<Actor> actors = filmDAO.getActorsByFilmId(999);
        assertTrue(actors.isEmpty());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Film_Actor;");
            statement.execute("DROP TABLE IF EXISTS Film;");
            statement.execute("DROP TABLE IF EXISTS Actor;");
        }
    }
}
