package org.example.servlet.mapper;

import org.example.model.Director;
import org.example.model.Entity;
import org.example.servlet.dto.DTO;
import org.example.servlet.dto.FilmDTO;
import org.example.model.Actor;
import org.example.model.Film;

import java.util.List;

public class FilmMapper implements Mapper {

    @Override
    public FilmDTO toDTO(Entity entity) {
        if (entity instanceof Film film) {
            List<String> actorNames = film.getActors().stream()
                    .map(Actor::getName)
                    .toList();
            return new FilmDTO(film.getId(), film.getTitle(), film.getReleaseYear(), actorNames, film.getDirector().getName());
        } else throw new IllegalArgumentException("Expected an Film entity");
    }

    @Override
    public Film toEntity(DTO dto) {
        if (dto instanceof FilmDTO filmDTO) {
            return new Film(filmDTO.id(), filmDTO.title(), filmDTO.releaseYear(), List.of(), new Director()); // актеры добавляются позже
        } else throw new IllegalArgumentException("Expected a FilmDTO");
    }
}
