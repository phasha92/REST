package org.example.servlet.dto;

import org.example.servlet.dto.ActorDTO;
import org.example.servlet.dto.FilmDTO;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DTOTest {

    @Test
    public void testActorDTOValid() {
        ActorDTO actor = new ActorDTO(1, "John Doe", List.of("Film A", "Film B"));
        assertEquals(1, actor.id());
        assertEquals("John Doe", actor.name());
        assertEquals(List.of("Film A", "Film B"), actor.filmTitles());
    }

    @Test
    public void testActorDTONullValues() {
        ActorDTO actor = new ActorDTO(1, null, null);
        assertEquals(1, actor.id());
        assertNull(actor.name());
        assertNull(actor.filmTitles());
    }

    @Test
    public void testActorDTOEmptyList() {
        ActorDTO actor = new ActorDTO(1, "Jane Doe", Collections.emptyList());
        assertEquals(1, actor.id());
        assertEquals("Jane Doe", actor.name());
        assertTrue(actor.filmTitles().isEmpty());
    }

    @Test
    public void testFilmDTOValid() {
        FilmDTO film = new FilmDTO(1, "Some Movie", 2020, List.of("Actor A", "Actor B"));
        assertEquals(1, film.id());
        assertEquals("Some Movie", film.title());
        assertEquals(2020, film.releaseYear());
        assertEquals(List.of("Actor A", "Actor B"), film.actorNames());
    }

    @Test
    public void testFilmDTONullValues() {
        FilmDTO film = new FilmDTO(1, null, 0, null);
        assertEquals(1, film.id());
        assertNull(film.title());
        assertEquals(0, film.releaseYear());
        assertNull(film.actorNames());
    }

    @Test
    public void testFilmDTOEmptyList() {
        FilmDTO film = new FilmDTO(1, "Some Movie", 2020, Collections.emptyList());
        assertEquals(1, film.id());
        assertEquals("Some Movie", film.title());
        assertEquals(2020, film.releaseYear());
        assertTrue(film.actorNames().isEmpty());
    }

    @Test
    public void testActorDTOEqualsAndHashCode() {
        ActorDTO actor1 = new ActorDTO(1, "John Doe", List.of("Film A"));
        ActorDTO actor2 = new ActorDTO(1, "Jane Doe", List.of("Film B"));
        ActorDTO actor3 = new ActorDTO(2, "John Doe", List.of("Film A"));

        assertEquals(actor1, actor1);
        assertNotEquals(actor1, null);
        assertNotEquals(actor1, actor2);
        assertNotEquals(actor1, actor3);
        assertEquals(actor1.hashCode(), actor2.hashCode());
        assertNotEquals(actor1.hashCode(), actor3.hashCode());
    }

    @Test
    public void testFilmDTOEqualsAndHashCode() {
        FilmDTO film1 = new FilmDTO(1, "Inception", 2010, List.of("Actor A"));
        FilmDTO film2 = new FilmDTO(1, "Interstellar", 2014, List.of("Actor B"));
        FilmDTO film3 = new FilmDTO(2, "Inception", 2010, List.of("Actor A"));

        assertEquals(film1, film1);
        assertNotEquals(film1, null);
        assertNotEquals(film1, film2);
        assertNotEquals(film1, film3);
        assertEquals(film1.hashCode(), film2.hashCode());
        assertNotEquals(film1.hashCode(), film3.hashCode());
    }

    @Test
    public void testInvalidActorDTO() {
        assertThrows(NullPointerException.class, () -> {
            new ActorDTO(1, "John Doe", null).filmTitles().get(0);
        });
    }

    @Test
    public void testInvalidFilmDTO() {
        assertThrows(NullPointerException.class, () -> {
            new FilmDTO(1, null, 2020, null).actorNames().get(0);
        });
    }
}
