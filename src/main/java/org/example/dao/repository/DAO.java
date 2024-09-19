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

    static void linkEntities(int filmId, int actorId) throws SQLException {
        String existLinkQuery = LinkedQuery.EXIST_LINK.getQuery();
        String insertQuery = LinkedQuery.INSERT_LINK.getQuery();

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement eStatement = connection.prepareStatement(existLinkQuery);
             PreparedStatement iStatement = connection.prepareStatement(insertQuery)) {

            // Проверка, существует ли уже такая связь
            eStatement.setInt(1, filmId);
            eStatement.setInt(2, actorId);
            try (ResultSet res = eStatement.executeQuery()) {
                if (res.next()) {
                    if (res.getInt(1) > 0) {
                        throw new SQLException("Film id(" + filmId + ") and actor id(" + actorId + ") are already linked");
                    }
                }

                // Если связи нет, то добавляем её
                iStatement.setInt(1, filmId);  // Порядок правильный: filmId -> actorId
                iStatement.setInt(2, actorId);
                iStatement.executeUpdate();
            }
        }
    }

    static void linkFilmWithActor(int filmId, int actorId) throws SQLException {
        // Проверка существования фильма и актёра
        if (!EntityExistenceChecker.isExist("Film", filmId)) {
            throw new SQLException("Film with id " + filmId + " does not exist.");
        }

        if (!EntityExistenceChecker.isExist("Actor", actorId)) {
            throw new SQLException("Actor with id " + actorId + " does not exist.");
        }

        // Связываем фильм с актёром через таблицу Film_Actor
        linkEntities(filmId, actorId);  // Передаём filmId первым
    }
}
