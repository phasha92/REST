package org.example.servlet.mapper;

import org.example.model.Entity;
import org.example.servlet.dto.ActorDTO;
import org.example.model.Actor;
import org.example.model.Film;
import org.example.servlet.dto.DTO;

import java.util.List;
import java.util.stream.Collectors;

public class ActorMapper implements Mapper {

    @Override
    public ActorDTO toDTO(Entity entity) {
        if (entity instanceof Actor actor) {
            List<String> filmTitles = actor.getFilms().stream()
                    .map(Film::getTitle)
                    .collect(Collectors.toList());
            return new ActorDTO(actor.getId(), actor.getName(), filmTitles);
        } else throw new IllegalArgumentException("Expected an Actor entity");

    }

    @Override
    public Actor toEntity(DTO dto) {
        if (dto instanceof ActorDTO actorDTO) {
            return new Actor(actorDTO.id(), actorDTO.name(), List.of()); // фильмы добавляются позже
        }
        throw new IllegalArgumentException("Expected an ActorDTO");
    }
}
