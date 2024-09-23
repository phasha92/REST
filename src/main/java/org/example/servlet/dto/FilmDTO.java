package org.example.servlet.dto;

import java.util.List;

public record FilmDTO(int id, String title, int releaseYear, List<String> actorNames,
                      String directorName) implements DTO {
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
