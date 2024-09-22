package org.example.servlet.dto;

import java.util.List;

public record DirectorDTO(int id, String name, List<String> films) implements DTO{
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
