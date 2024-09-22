package org.example.servlet.mapper;

import org.example.model.Actor;
import org.example.model.Film;
import org.example.servlet.dto.ActorDTO;
import org.example.servlet.dto.FilmDTO;
import org.example.servlet.mapper.FilmMapper;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmMapperTest {

    private final FilmMapper filmMapper = new FilmMapper();

    @Test
    public void testToDTO_ValidFilm() {
        Film film = new Film(1, "Film A", 2020, List.of(new Actor(1, "John Doe", Collections.emptyList())));
        FilmDTO dto = filmMapper.toDTO(film);
        assertEquals(1, dto.id());
        assertEquals("Film A", dto.title());
        assertEquals(2020, dto.releaseYear());
        assertEquals(List.of("John Doe"), dto.actorNames());
    }

    @Test
    public void testToEntity_ValidDTO() {
        FilmDTO dto = new FilmDTO(1, "Film A", 2020, List.of("John Doe"));
        Film film = filmMapper.toEntity(dto);
        assertEquals(1, film.getId());
        assertEquals("Film A", film.getTitle());
        assertEquals(2020, film.getReleaseYear());
    }

    @Test
    public void testToDTO_InvalidEntity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            filmMapper.toDTO(new Actor(1, "John Doe", Collections.emptyList()));
        });
        assertEquals("Expected an Film entity", exception.getMessage());
    }

    @Test
    public void testToEntity_InvalidDTO() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            filmMapper.toEntity(new ActorDTO(1, "John Doe", List.of("Film A")));
        });
        assertEquals("Expected a FilmDTO", exception.getMessage());
    }
}
