package org.example.service;

import org.example.dao.repository.DAO;

import java.sql.SQLException;
import java.util.List;

public interface EntityService<T> {
    T getById(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    default void linkFilmWithActor(int filmId, int actorId) throws SQLException{
        DAO.linkFilmWithActor(filmId, actorId);
    }
}
