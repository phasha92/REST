package org.example.servlet.mapper;

import org.example.model.Entity;
import org.example.servlet.dto.DTO;
import org.example.servlet.dto.FilmDTO;
import org.example.model.Actor;
import org.example.model.Film;

import java.util.List;
import java.util.stream.Collectors;

public class FilmMapper implements Mapper {

    @Override
    public FilmDTO toDTO(Entity entity) {
        if (entity instanceof Film film) {
            List<String> actorNames = film.getActors().stream()
                    .map((object) -> (Actor) object)
                    .map(Actor::getName)
                    .collect(Collectors.toList());
            return new FilmDTO(film.getId(), film.getTitle(), film.getReleaseYear(), actorNames);
        } else throw new IllegalArgumentException("Expected an Film entity");
    }

    @Override
    public Film toEntity(DTO dto) {
        if (dto instanceof FilmDTO filmDTO) {
            return new Film(filmDTO.id(), filmDTO.title(), filmDTO.releaseYear(), List.of()); // актеры добавляются позже
        } else throw new IllegalArgumentException("Expected a FilmDTO");
    }
}
