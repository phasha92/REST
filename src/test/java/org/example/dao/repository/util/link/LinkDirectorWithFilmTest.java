package org.example.dao.repository.util.link;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.util.EntityExistenceChecker;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class LinkDirectorWithFilmTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private LinkDirectorWithFilm linkDirectorWithFilm;
    private DBConnectManager dbConnectManager;
    private EntityExistenceChecker checker;

    @BeforeEach
    public void setUp() throws SQLException {
        dbConnectManager = new DBConnectManager(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());

        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Film (id SERIAL PRIMARY KEY, director_id INT);");
            statement.execute("CREATE TABLE IF NOT EXISTS Director (id SERIAL PRIMARY KEY);");

            statement.execute("INSERT INTO Director (id) VALUES (1);");
            statement.execute("INSERT INTO Film (id, director_id) VALUES (1, NULL);");
        }
        checker = new EntityExistenceChecker(dbConnectManager);
        linkDirectorWithFilm = new LinkDirectorWithFilm(dbConnectManager, checker);
    }

    @Test
    void testLinkEntities_Success() throws SQLException {
        linkDirectorWithFilm.linkEntities(1, 1);
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT director_id FROM Film WHERE id = 1;")) {
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt("director_id"));
        }
    }

    @Test
    void testLinkEntities_AlreadyLinked() {
        assertDoesNotThrow(() -> linkDirectorWithFilm.linkEntities(1, 1));
        SQLException exception = assertThrows(SQLException.class, () -> linkDirectorWithFilm.linkEntities(1, 1));
        assertEquals("Film id(1) and director id(1) are already linked", exception.getMessage());
    }

    @Test
    void testLinkFilmWithDirector_FilmDoesNotExist() {
        SQLException exception = assertThrows(SQLException.class, () -> linkDirectorWithFilm.linkFilmWithDirector(2222, 1));
        assertEquals("Film with id 2222 does not exist.", exception.getMessage());
    }

    @Test
    void testLinkFilmWithDirector_DirectorDoesNotExist() {
        SQLException exception = assertThrows(SQLException.class, () -> linkDirectorWithFilm.linkFilmWithDirector(1, 2222));
        assertEquals("Director with id 2222 does not exist.", exception.getMessage());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM Film;");
            statement.execute("DELETE FROM Director;");
        }
    }
}

