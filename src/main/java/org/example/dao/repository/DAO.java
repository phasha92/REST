package org.example.dao.repository;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.query.LinkedQuery;
import org.example.dao.repository.util.EntityExistenceChecker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    void create(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    void delete(int id) throws SQLException;

    T getById(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    private void linkEntities(int firstId, int secondId) throws SQLException {

        String insertQuery = LinkedQuery.INSERT_LINK.getQuery();
        String existLinkQuery = LinkedQuery.EXIST_LINK.getQuery();
        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(existLinkQuery);
             PreparedStatement statement1 = connection.prepareStatement(insertQuery)) {

            statement.setInt(1, firstId);
            statement.setInt(2, secondId);
            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) if (res.getInt(1) == 0) {

                    statement1.setInt(1, firstId);
                    statement1.setInt(2, secondId);
                    statement1.executeUpdate();
                }
            }

        }
    }

    default void linkFilmWithActor(int filmId, int actorId) throws SQLException {

        // Проверка, существует ли фильм и актёр
        if (!EntityExistenceChecker.isExist("Film", filmId)) {
            throw new SQLException("Film with id " + filmId + " does not exist.");
        }

        if (!EntityExistenceChecker.isExist("Actor", actorId)) {
            throw new SQLException("Actor with id " + actorId + " does not exist.");
        }

        // Связываем фильм с актёром через таблицу Film_Actor
        linkEntities(filmId, actorId);
    }
}
