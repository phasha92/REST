package org.example.servlet.mapper;

import org.example.model.Actor;
import org.example.model.Director;
import org.example.model.Film;
import org.example.servlet.dto.ActorDTO;
import org.example.servlet.dto.FilmDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmMapperTest {

    private final FilmMapper filmMapper = new FilmMapper();

    @Test
    void testToDTO_ValidFilm() {
        Director director = new Director(1, "Mr. Smith", new ArrayList<>());
        Film film = new Film(1, "Film A", 2020,
                List.of(new Actor(1, "John Doe", Collections.emptyList())), director);
        FilmDTO dto = filmMapper.toDTO(film);

        assertEquals(1, dto.id());
        assertEquals("Film A", dto.title());
        assertEquals(2020, dto.releaseYear());
        assertEquals(List.of("John Doe"), dto.actorNames());
        assertEquals("Mr. Smith", dto.directorName());
    }

    @Test
    void testToEntity_ValidDTO() {
        FilmDTO dto = new FilmDTO(1, "Film A", 2020, List.of("John Doe"), "Mr. Smith");
        Film film = filmMapper.toEntity(dto);

        assertEquals(1, film.getId());
        assertEquals("Film A", film.getTitle());
        assertEquals(2020, film.getReleaseYear());
        assertEquals("Mr. Smith", film.getDirector().getName());
    }

    @Test
    void testToDTO_InvalidEntity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            filmMapper.toDTO(new Actor(1, "John Doe", Collections.emptyList()));
        });
        assertEquals("Expected an Film entity", exception.getMessage());
    }

    @Test
    void testToEntity_InvalidDTO() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            filmMapper.toEntity(new ActorDTO(1, "John Doe", List.of("Film A")));
        });
        assertEquals("Expected a FilmDTO", exception.getMessage());
    }
}


