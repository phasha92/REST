package org.example.servlet.mapper;

public interface Mapper {
    Object toDTO(Object entity);
    Object toEntity(Object dto);
}