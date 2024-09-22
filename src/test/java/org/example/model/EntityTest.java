package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {
    private Actor actor;
    private Film film;
    private Director director;

    @BeforeEach
    void setUp() {
        actor = new Actor(1, "John Doe", new ArrayList<>());
        director = new Director(1, "Mr. Smith", new ArrayList<>());
        film = new Film(1, "Sample Film", 2020, new ArrayList<>(), director);
    }

    @Test
    void testGettersAndSetters() {
        // Actor tests
        assertEquals(1, actor.getId());
        assertEquals("John Doe", actor.getName());
        actor.setName("Jane Doe");
        assertEquals("Jane Doe", actor.getName());

        // Film tests
        assertEquals(1, film.getId());
        assertEquals("Sample Film", film.getTitle());
        film.setTitle("Another Film");
        assertEquals("Another Film", film.getTitle());
        film.setReleaseYear(2021);
        assertEquals(2021, film.getReleaseYear());

        // Director tests
        assertEquals(1, director.getId());
        assertEquals("Mr. Smith", director.getName());
        director.setName("Andrew");
        assertEquals("Andrew", director.getName());
    }

    @Test
    void testUnmodifiableLists() {
        List<Actor> actors = film.getActors();
        assertThrows(UnsupportedOperationException.class, () -> actors.add(new Actor()));

        List<Film> films = actor.getFilms();
        assertThrows(UnsupportedOperationException.class, () -> films.add(new Film()));

        List<Film> directorFilms = director.getFilms();
        assertThrows(UnsupportedOperationException.class, () -> directorFilms.add(new Film()));
    }

    @Test
    void testSetActors() {
        Film film = new Film();
        List<Actor> actors = Arrays.asList(
                new Actor(1, "Actor 1", null),
                new Actor(2, "Actor 2", null));
        film.setActors(actors);
        assertEquals(2, film.getActors().size());
        assertEquals("Actor 1", film.getActors().get(0).getName());

        film.setActors(new ArrayList<>());
        assertTrue(film.getActors().isEmpty());

        film.setActors(null);
        assertNotNull(film.getActors());
    }

    @Test
    void testSetFilms() {
        Actor actor = new Actor();
        List<Film> films = Arrays.asList(
                new Film(1, "Film 1", 2020, null, null),
                new Film(2, "Film 2", 2021, null, null));
        actor.setFilms(films);
        assertEquals(2, actor.getFilms().size());
        assertEquals("Film 1", actor.getFilms().get(0).getTitle());

        actor.setFilms(new ArrayList<>());
        assertTrue(actor.getFilms().isEmpty());

        actor.setFilms(null);
        assertNotNull(actor.getFilms());
    }

    @Test
    void testSetFilmsInDirector() {
        List<Film> films = Arrays.asList(
                new Film(1, "Film 1", 2020, null, null),
                new Film(2, "Film 2", 2021, null, null));
        director.setFilms(films);
        assertEquals(2, director.getFilms().size());
        assertEquals("Film 1", director.getFilms().get(0).getTitle());

        director.setFilms(new ArrayList<>());
        assertTrue(director.getFilms().isEmpty());

        director.setFilms(null);
        assertNotNull(director.getFilms());
    }

    @Test
    void testEqualityAndHashCode() {
        Actor actor1 = new Actor(1, "John Doe", null);
        Actor actor2 = new Actor(1, "Jane Doe", null);
        Actor actor3 = new Actor(2, "John Doe", null);
        assertEquals(actor1, actor1, "Object should be equal to itself");
        assertNotEquals(null, actor1, "Object should not be equal to null");
        Film film = new Film(1, "Some Movie", 2020, null, null);
        assertNotEquals(actor1, film, "Object should not be equal to an object of another class");
        assertEquals(actor1, actor2, "Objects with the same id should be equal");
        assertEquals(actor1.hashCode(), actor2.hashCode(), "Hash codes of objects with the same id should match");
        assertNotEquals(actor1, actor3, "Objects with different ids should not be equal");
        assertNotEquals(actor1.hashCode(), actor3.hashCode(), "Hash codes of objects with different ids should not match");

        Film film1 = new Film(1, "Inception", 2010, null, null);
        Film film2 = new Film(1, "Interstellar", 2014, null, null);
        Film film3 = new Film(2, "Inception", 2010, null, null);
        assertEquals(film1, film1, "Object should be equal to itself");
        assertNotEquals(null, film1, "Object should not be equal to null");
        Actor actor = new Actor(1, "John Doe", null);
        assertNotEquals(film1, actor, "Object should not be equal to an object of another class");
        assertEquals(film1, film2, "Objects with the same id should be equal");
        assertEquals(film1.hashCode(), film2.hashCode(), "Hash codes of objects with the same id should match");
        assertNotEquals(film1, film3, "Objects with different ids should not be equal");
        assertNotEquals(film1.hashCode(), film3.hashCode(), "Hash codes of objects with different ids should not match");

        // Director tests
        Director director1 = new Director(1, "Mr. Smith", null);
        Director director2 = new Director(1, "Mr. Johnson", null);
        Director director3 = new Director(2, "Mr. Smith", null);
        assertEquals(director1, director1, "Object should be equal to itself");
        assertNotEquals(null, director1, "Object should not be equal to null");
        Film directorFilm = new Film(1, "Some Movie", 2020, null, null);
        assertNotEquals(director1, directorFilm, "Object should not be equal to an object of another class");
        assertEquals(director1, director2, "Objects with the same id should be equal");
        assertEquals(director1.hashCode(), director2.hashCode(), "Hash codes of objects with the same id should match");
        assertNotEquals(director1, director3, "Objects with different ids should not be equal");
        assertNotEquals(director1.hashCode(), director3.hashCode(), "Hash codes of objects with different ids should not match");
    }

    @Test
    void testToString() {
        String actorString = actor.toString();
        assertTrue(actorString.contains("id=1"));
        assertTrue(actorString.contains("name='John Doe'"));

        String filmString = film.toString();
        assertTrue(filmString.contains("id=1"));
        assertTrue(filmString.contains("title='Sample Film'"));

        String directorString = director.toString();
        assertTrue(directorString.contains("id=1"));
        assertTrue(directorString.contains("name='Mr. Smith'"));
    }

    @Test
    void testNullValues() {
        Actor actorWithNull = new Actor(2, null, null);
        assertNull(actorWithNull.getName());
        assertTrue(actorWithNull.getFilms().isEmpty());

        Film filmWithNull = new Film(2, null, 0, null, null);
        assertNull(filmWithNull.getTitle());
        assertTrue(filmWithNull.getActors().isEmpty());

        Director directorWithNull = new Director(2, null, null);
        assertNull(directorWithNull.getName());
        assertTrue(directorWithNull.getFilms().isEmpty());
    }

    @Test
    void testEmptyLists() {
        Actor actorWithEmptyFilms = new Actor(3, "Empty Films Actor", new ArrayList<>());
        assertTrue(actorWithEmptyFilms.getFilms().isEmpty());

        Film filmWithEmptyActors = new Film(3, "Empty Actors Film", 2021, new ArrayList<>(), null);
        assertTrue(filmWithEmptyActors.getActors().isEmpty());

        Director directorWithEmptyFilms = new Director(3, "Empty Films Director", new ArrayList<>());
        assertTrue(directorWithEmptyFilms.getFilms().isEmpty());
    }

    @Test
    void testInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> new Actor(-1, "Invalid Actor", new ArrayList<>()));
        assertThrows(IllegalArgumentException.class, () -> new Film(-1, "Invalid Film", 2020, new ArrayList<>(), null));
        assertThrows(IllegalArgumentException.class, () -> new Director(-1, "Invalid Director", new ArrayList<>()));
    }
}

