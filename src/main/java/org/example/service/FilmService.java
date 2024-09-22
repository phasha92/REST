package org.example.service;

import org.example.model.Film;
import org.example.servlet.dto.FilmDTO;

import java.sql.SQLException;

public interface FilmService extends EntityService<Film> {
    void create(FilmDTO filmDTO) throws SQLException;

    void update(FilmDTO filmDTO) throws SQLException;

    void delete(int id) throws SQLException;

    void addActorToFilm(int filmId, int actorId) throws SQLException;

    void addDirectorToFilm(int filmId, int directorId) throws SQLException;
}
