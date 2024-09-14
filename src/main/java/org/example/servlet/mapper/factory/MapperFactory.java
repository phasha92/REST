package org.example.servlet.mapper.factory;

import org.example.model.Entity;
import org.example.servlet.dto.DTO;
import org.example.servlet.mapper.Mapper;

public interface MapperFactory {
    Mapper getMapperForEntity(Entity entity);
    Mapper getMapperForDTO(DTO dto);
}
