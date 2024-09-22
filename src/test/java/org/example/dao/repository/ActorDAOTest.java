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
public class ActorDAOTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private ActorDAO actorDAO;
    private DBConnectManager dbConnectManager;

    @BeforeEach
    public void setUp() throws SQLException {
        dbConnectManager = new DBConnectManager(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());

        // Создаем таблицы Actor, Film и Film_Actor
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Director (id SERIAL PRIMARY KEY, name VARCHAR(255));");
            statement.execute("CREATE TABLE IF NOT EXISTS Actor (id SERIAL PRIMARY KEY, name VARCHAR(255));");
            statement.execute("CREATE TABLE IF NOT EXISTS Film (id SERIAL PRIMARY KEY, title VARCHAR(255), release_year INT, director_id INT);");
            statement.execute("CREATE TABLE IF NOT EXISTS Film_Actor (film_id INT, actor_id INT, PRIMARY KEY (film_id, actor_id));");

            actorDAO = new ActorDAO(dbConnectManager);
        }
    }

    @Test
    public void testCreateActor() throws SQLException {
        Actor actor = new Actor();
        actor.setName("Test Actor");
        actorDAO.create(actor);

        assertNotNull(actor.getId());
        Actor retrievedActor = actorDAO.getById(actor.getId());
        assertEquals("Test Actor", retrievedActor.getName());
    }

    @Test
    public void testGetById() throws SQLException {
        Actor actor = new Actor();
        actor.setName("Test Actor");
        actorDAO.create(actor);

        Actor retrievedActor = actorDAO.getById(actor.getId());
        assertNotNull(retrievedActor);
        assertEquals(actor.getName(), retrievedActor.getName());
    }

    @Test
    public void testUpdateActor() throws SQLException {
        Actor actor = new Actor();
        actor.setName("Test Actor");
        actorDAO.create(actor);

        actor.setName("Updated Actor");
        actorDAO.update(actor);

        Actor updatedActor = actorDAO.getById(actor.getId());
        assertEquals("Updated Actor", updatedActor.getName());
    }

    @Test
    public void testDeleteActor() throws SQLException {
        Actor actor = new Actor();
        actor.setName("Test Actor");
        actorDAO.create(actor);

        actorDAO.delete(actor.getId());
        Actor deletedActor = actorDAO.getById(actor.getId());
        assertNull(deletedActor);
    }

    @Test
    public void testGetAllActors() throws SQLException {
        Actor actor1 = new Actor();
        actor1.setName("Actor 1");
        Actor actor2 = new Actor();
        actor2.setName("Actor 2");

        actorDAO.create(actor1);
        actorDAO.create(actor2);

        List<Actor> actors = actorDAO.getAll();
        assertEquals(2, actors.size());
    }

    @Test
    public void testGetByIdNonExistentActorReturnsNull() throws SQLException {
        Actor retrievedActor = actorDAO.getById(999);
        assertNull(retrievedActor);
    }

    @Test
    public void testGetFilmsByActorId_WithFilms() throws SQLException {
        Actor actor = new Actor();
        actor.setName("Test Actor");
        actorDAO.create(actor);

        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO Film (title, release_year, director_id) VALUES ('Film 1', 2020, 1);");
            statement.execute("INSERT INTO Film (title, release_year, director_id) VALUES ('Film 2', 2021, 1);");
            statement.execute("INSERT INTO Film_Actor (film_id, actor_id) VALUES (1, " + actor.getId() + ");");
            statement.execute("INSERT INTO Film_Actor (film_id, actor_id) VALUES (2, " + actor.getId() + ");");
        }

        List<Film> films = actorDAO.getFilmsByActorId(actor.getId());
        assertEquals(2, films.size());
        assertEquals("Film 1", films.get(0).getTitle());
        assertEquals("Film 2", films.get(1).getTitle());
    }

    @Test
    public void testGetFilmsByActorId_NoFilms() throws SQLException {
        Actor actor = new Actor();
        actor.setName("Test Actor No Films");
        actorDAO.create(actor);

        List<Film> films = actorDAO.getFilmsByActorId(actor.getId());
        assertTrue(films.isEmpty());
    }

    @Test
    public void testGetFilmsByActorId_NonExistentActor() throws SQLException {
        List<Film> films = actorDAO.getFilmsByActorId(999);
        assertTrue(films.isEmpty());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Film_Actor;");
            statement.execute("DROP TABLE IF EXISTS Film;");
            statement.execute("DROP TABLE IF EXISTS Actor;");
            statement.execute("DROP TABLE IF EXISTS Director;");
        }
    }
}
