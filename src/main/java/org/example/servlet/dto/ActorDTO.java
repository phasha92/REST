package org.example.servlet.dto;

import java.util.List;

public record ActorDTO(int id, String name, List<String> filmTitles) implements DTO {
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}

