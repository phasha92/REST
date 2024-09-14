package org.example.servlet.mapper;

import org.example.servlet.dto.FilmDTO;
import org.example.model.Actor;
import org.example.model.Film;

import java.util.List;
import java.util.stream.Collectors;

public class FilmMapper implements Mapper {

    @Override
    public Object toDTO(Object entity) {
        if (!(entity instanceof Film film)) {
            throw new IllegalArgumentException("Expected a Film entity");
        }
        List<String> actorNames = film.getActors().stream()
                .map(Actor::getName)
                .collect(Collectors.toList());

        return new FilmDTO(film.getId(), film.getTitle(), film.getReleaseYear(), actorNames);
    }

    @Override
    public Object toEntity(Object dto) {
        if (!(dto instanceof FilmDTO filmDTO)) {
            throw new IllegalArgumentException("Expected a FilmDTO");
        }
        return new Film(filmDTO.id(), filmDTO.title(), filmDTO.releaseYear(), List.of()); // актеры добавляются позже
    }
}
