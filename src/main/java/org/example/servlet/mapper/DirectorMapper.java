package org.example.servlet.mapper;

import org.example.model.Director;
import org.example.model.Entity;
import org.example.model.Film;
import org.example.servlet.dto.DTO;
import org.example.servlet.dto.DirectorDTO;

import java.util.List;

public class DirectorMapper implements Mapper {
    @Override
    public DirectorDTO toDTO(Entity entity) {
        if (entity instanceof Director director) {
            List<String> filmTitles = director.getFilms().stream()
                    .map(Film::getTitle)
                    .toList();
            return new DirectorDTO(director.getId(), director.getName(), filmTitles);
        } else throw new IllegalArgumentException("Expected an Actor entity");
    }

    @Override
    public Director toEntity(DTO dto) {
        if (dto instanceof DirectorDTO directorDTO) {
            return new Director(directorDTO.id(), directorDTO.name(), List.of());
        } else throw new IllegalArgumentException("Expected a DirectorDTO");
    }
}
