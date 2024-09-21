package org.example.DAOTest.RepositoryTest;

import static org.junit.jupiter.api.Assertions.*;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.ActorDAO;
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

        // Создаем таблицы Actor и Film
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Actor (id SERIAL PRIMARY KEY, name VARCHAR(255));");
            statement.execute("CREATE TABLE IF NOT EXISTS Film (id SERIAL PRIMARY KEY, title VARCHAR(255), release_year INT);");
            statement.execute("CREATE TABLE IF NOT EXISTS Film_Actor (film_id INT, actor_id INT, PRIMARY KEY (film_id, actor_id));");

            actorDAO = new ActorDAO(dbConnectManager); // Используем новый конструктор
        }
    }

    @Test
    public void testCreateActor() throws SQLException {
        Actor actor = new Actor();
        actor.setName("Test Actor");
        actorDAO.create(actor);

        assertNotNull(actor.getId()); // Проверяем, что ID присвоен
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
        assertNull(deletedActor); // Проверяем, что актёр удалён
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
        Actor retrievedActor = actorDAO.getById(999); // Запрос к несуществующему ID
        assertNull(retrievedActor);
    }

    @Test
    public void testGetFilmsByActorId_WithFilms() throws SQLException {
        // Создаем актёра
        Actor actor = new Actor();
        actor.setName("Test Actor");
        actorDAO.create(actor);

        // Создаем фильм и связываем его с актёром
        try (Connection connection = dbConnectManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO Film (title, release_year) VALUES ('Film 1', 2020);");
            statement.execute("INSERT INTO Film (title, release_year) VALUES ('Film 2', 2021);");
            statement.execute("INSERT INTO Film_Actor (film_id, actor_id) VALUES (1, " + actor.getId() + ");");
            statement.execute("INSERT INTO Film_Actor (film_id, actor_id) VALUES (2, " + actor.getId() + ");");
        }

        // Проверяем получение фильмов
        List<Film> films = actorDAO.getFilmsByActorId(actor.getId());
        assertEquals(2, films.size());
        assertEquals("Film 1", films.get(0).getTitle());
        assertEquals("Film 2", films.get(1).getTitle());
    }

    @Test
    public void testGetFilmsByActorId_NoFilms() throws SQLException {
        // Создаем актёра без фильмов
        Actor actor = new Actor();
        actor.setName("Test Actor No Films");
        actorDAO.create(actor);

        // Проверяем получение фильмов
        List<Film> films = actorDAO.getFilmsByActorId(actor.getId());
        assertTrue(films.isEmpty()); // Должен вернуть пустой список
    }

    @Test
    public void testGetFilmsByActorId_NonExistentActor() throws SQLException {
        // Проверяем получение фильмов для несуществующего актёра
        List<Film> films = actorDAO.getFilmsByActorId(999); // Не существующий ID
        assertTrue(films.isEmpty()); // Должен вернуть пустой список
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
