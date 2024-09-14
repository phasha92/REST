package org.example.servlet.mapper.factory;

import org.example.servlet.mapper.Mapper;

public interface MapperFactory {
    Mapper getMapperForEntity(Object entity);
    Mapper getMapperForDTO(Object dto);
}
