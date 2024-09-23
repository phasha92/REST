package org.example.dao.repository;

import org.example.dao.DBConnectManager;
import org.example.model.Director;
import org.example.model.Film;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class DirectorDAOTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private DirectorDAO directorDAO;
    private DBConnectManager dbConnectManager;

    @BeforeEach
    public void setUp() throws SQLException {
        dbConnectManager = new DBConnectManager(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());

        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Actor (id SERIAL PRIMARY KEY, name VARCHAR(255));");
            statement.execute("CREATE TABLE IF NOT EXISTS Film (id SERIAL PRIMARY KEY, title VARCHAR(255), release_year INT, director_id INT);");
            statement.execute("CREATE TABLE IF NOT EXISTS Film_Actor (film_id INT, actor_id INT, PRIMARY KEY (film_id, actor_id));");
            statement.execute("CREATE TABLE IF NOT EXISTS Director (id SERIAL PRIMARY KEY, name VARCHAR(255));");
            directorDAO = new DirectorDAO(dbConnectManager);
        }
    }

    @Test
    void testCreateDirector() throws SQLException {
        Director director = new Director();
        director.setName("Test Director");
        directorDAO.create(director);
        Director retrievedDirector = directorDAO.getById(director.getId());
        assertEquals("Test Director", retrievedDirector.getName());
    }

    @Test
    void testGetById() throws SQLException {
        Director director = new Director();
        director.setName("Test Director");
        directorDAO.create(director);
        Director retrievedDirector = directorDAO.getById(director.getId());
        assertNotNull(retrievedDirector);
        assertEquals(director.getName(), retrievedDirector.getName());
    }

    @Test
    void testUpdateDirector() throws SQLException {
        Director director = new Director();
        director.setName("Test Director");
        directorDAO.create(director);
        director.setName("Updated Director");
        directorDAO.update(director);
        Director updatedDirector = directorDAO.getById(director.getId());
        assertEquals("Updated Director", updatedDirector.getName());
    }

    @Test
    void testDeleteDirector() throws SQLException {
        Director director = new Director();
        director.setName("Test Director");
        directorDAO.create(director);
        directorDAO.delete(director.getId());
        Director deletedDirector = directorDAO.getById(director.getId());
        assertNull(deletedDirector);
    }

    @Test
    void testGetAllDirectors() throws SQLException {
        Director director1 = new Director();
        director1.setName("Director 1");
        Director director2 = new Director();
        director2.setName("Director 2");
        directorDAO.create(director1);
        directorDAO.create(director2);
        List<Director> directors = directorDAO.getAll();
        assertEquals(2, directors.size());
    }

    @Test
    void testGetByIdNonExistentDirectorReturnsNull() throws SQLException {
        Director retrievedDirector = directorDAO.getById(999);
        assertNull(retrievedDirector);
    }

    @Test
    void testGetFilmsByDirectorId_WithFilms() throws SQLException {
        Director director = new Director();
        director.setName("Test Director");
        directorDAO.create(director);
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO Film (title, release_year, director_id) VALUES ('Film 1', 2020, " + director.getId() + ");");
            statement.execute("INSERT INTO Film (title, release_year, director_id) VALUES ('Film 2', 2021, " + director.getId() + ");");
        }

        List<Film> films = directorDAO.getFilmsByDirectorId(director.getId());
        assertEquals(2, films.size());
        assertEquals("Film 1", films.get(0).getTitle());
        assertEquals("Film 2", films.get(1).getTitle());
    }

    @Test
    void testGetFilmsByDirectorId_NoFilms() throws SQLException {
        Director director = new Director();
        director.setName("Test Director No Films");
        directorDAO.create(director);
        List<Film> films = directorDAO.getFilmsByDirectorId(director.getId());
        assertTrue(films.isEmpty());
    }

    @Test
    void testGetFilmsByDirectorId_NonExistentDirector() throws SQLException {
        List<Film> films = directorDAO.getFilmsByDirectorId(999);
        assertTrue(films.isEmpty());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Director;");
            statement.execute("DROP TABLE IF EXISTS Film_Actor;");
            statement.execute("DROP TABLE IF EXISTS Film;");
            statement.execute("DROP TABLE IF EXISTS Actor;");
        }
    }
}
