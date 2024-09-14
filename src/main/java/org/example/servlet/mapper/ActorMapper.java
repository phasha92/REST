package org.example.servlet.mapper;

import org.example.servlet.dto.ActorDTO;
import org.example.model.Actor;
import org.example.model.Film;

import java.util.List;
import java.util.stream.Collectors;

public class ActorMapper implements Mapper {

    @Override
    public Object toDTO(Object entity) {
        if (!(entity instanceof Actor actor)) {
            throw new IllegalArgumentException("Expected an Actor entity");
        }
        List<String> filmTitles = actor.getFilms().stream()
                .map(Film::getTitle)
                .collect(Collectors.toList());

        return new ActorDTO(actor.getId(), actor.getName(), filmTitles);
    }

    @Override
    public Object toEntity(Object dto) {
        if (!(dto instanceof ActorDTO actorDTO)) {
            throw new IllegalArgumentException("Expected an ActorDTO");
        }
        return new Actor(actorDTO.id(), actorDTO.name(), List.of()); // фильмы добавляются позже
    }
}
