package org.example.servlet.mapper;

import org.example.model.Entity;
import org.example.servlet.dto.DTO;

public interface Mapper {
    DTO toDTO(Entity entity);

    Entity toEntity(DTO dto);
}