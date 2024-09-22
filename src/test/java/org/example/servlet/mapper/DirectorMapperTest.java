package org.example.servlet.mapper;

import org.example.model.Director;
import org.example.model.Film;
import org.example.servlet.dto.DirectorDTO;
import org.example.servlet.dto.FilmDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DirectorMapperTest {

    private final DirectorMapper directorMapper = new DirectorMapper();

    @Test
    public void testToDTO_ValidDirector() {
        Director director =
                new Director(1, "Mr. Smith", List.of(
                        new Film(1, "Film A", 2020, Collections.emptyList(),
                                new Director(1, "Mr. Smith", new ArrayList<>()
                                ))));
        DirectorDTO dto = directorMapper.toDTO(director);

        assertEquals(1, dto.id());
        assertEquals("Mr. Smith", dto.name());
        assertEquals(List.of("Film A"), dto.films());
    }

    @Test
    public void testToEntity_ValidDTO() {
        DirectorDTO dto = new DirectorDTO(1, "Mr. Smith", List.of("Film A"));
        Director director = directorMapper.toEntity(dto);

        assertEquals(1, director.getId());
        assertEquals("Mr. Smith", director.getName());
    }

    @Test
    public void testToDTO_InvalidEntity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            directorMapper.toDTO(new Film(1, "Film A", 2020, Collections.emptyList(), new Director(1, "Mr. Smith", new ArrayList<>())));
        });
        assertEquals("Expected an Actor entity", exception.getMessage());
    }

    @Test
    public void testToEntity_InvalidDTO() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            directorMapper.toEntity(new FilmDTO(1, "Film A", 2020, List.of("John Doe"), "mr. Smith"));
        });
        assertEquals("Expected a DirectorDTO", exception.getMessage());
    }
}
