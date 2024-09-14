package org.example.servlet.mapper.factory;

import org.example.servlet.dto.ActorDTO;
import org.example.servlet.dto.FilmDTO;
import org.example.model.Actor;
import org.example.model.Film;
import org.example.servlet.mapper.ActorMapper;
import org.example.servlet.mapper.FilmMapper;
import org.example.servlet.mapper.Mapper;

public class MapperFactoryImp implements MapperFactory {

    @Override
    public Mapper getMapperForEntity(Object entity) {

        return switch (entity) {
            case Film film -> new FilmMapper();
            case Actor actor -> new ActorMapper();
            default -> throw new IllegalArgumentException("Unsupported entity type");
        };

    }

    @Override
    public Mapper getMapperForDTO(Object dto) {
        return switch (dto) {
            case FilmDTO filmDTO -> new FilmMapper();
            case ActorDTO actorDTO -> new ActorMapper();
            default -> throw new IllegalArgumentException("Unsupported DTO type");
        };
    }
}

