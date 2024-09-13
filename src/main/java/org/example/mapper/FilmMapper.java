package org.example.mapper;

import org.example.dto.FilmDTO;
import org.example.entity.Film;

public class FilmMapper implements Mapper<Film, FilmDTO> {
    @Override
    public FilmDTO toDTO(Film film) {
        return null;
    }

    @Override
    public Film fromDTO(FilmDTO filmDTO) {
        return null;
    }
}
