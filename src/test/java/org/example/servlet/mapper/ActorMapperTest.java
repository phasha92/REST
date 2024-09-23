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

class ActorMapperTest {

    private final ActorMapper actorMapper = new ActorMapper();

    @Test
    void testToDTO_ValidActor() {
        Actor actor =
                new Actor(1, "John Doe", List.of(
                        new Film(1, "Film A", 2020, Collections.emptyList(),
                                new Director(1, "Mr. Smith", new ArrayList<>()
                                ))));
        ActorDTO dto = actorMapper.toDTO(actor);
        assertEquals(1, dto.id());
        assertEquals("John Doe", dto.name());
        assertEquals(List.of("Film A"), dto.filmTitles());
    }

    @Test
    void testToEntity_ValidDTO() {
        ActorDTO dto = new ActorDTO(1, "John Doe", List.of("Film A"));
        Actor actor = actorMapper.toEntity(dto);
        assertEquals(1, actor.getId());
        assertEquals("John Doe", actor.getName());
    }

    @Test
    void testToDTO_InvalidEntity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            actorMapper.toDTO(
                    new Film(1, "Film A", 2020, Collections.emptyList(),
                            new Director(1, "Mr. Smith", new ArrayList<>()
                            )));
        });
        assertEquals("Expected an Actor entity", exception.getMessage());
    }

    @Test
    void testToEntity_InvalidDTO() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            actorMapper.toEntity(new FilmDTO(1, "Film A", 2020, List.of("John Doe"), "Mr. Smith"));
        });
        assertEquals("Expected an ActorDTO", exception.getMessage());
    }
}
