package org.example.service;

import org.example.model.Film;

import java.sql.SQLException;

public interface FilmService extends EntityService<Film> {
    void create(Film film) throws SQLException;

    void update(Film film) throws SQLException;

    void delete(int id) throws SQLException;
}
