package org.example.dao.repository;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.query.LinkedQuery;
import org.example.dao.repository.util.EntityExistenceChecker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface LinkActorWithFilm {
    default void linkEntities(int filmId, int actorId) throws SQLException {

        String existLinkQuery = LinkedQuery.EXIST_LINK.getQuery();
        String insertQuery = LinkedQuery.INSERT_LINK.getQuery();

        Connection connection = null;
        PreparedStatement eStatement = null;
        PreparedStatement iStatement = null;

        try {
            connection = DBConnectManager.getConnection();
            eStatement = connection.prepareStatement(existLinkQuery);
            iStatement = connection.prepareStatement(insertQuery);
            connection.setAutoCommit(false);
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
                connection.commit();
            }
        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            throw e;  // Пробрасываем исключение дальше
        } finally {
            if (iStatement != null) iStatement.close();
            if (eStatement != null) eStatement.close();
            if (connection != null) connection.close();
        }
    }

    default void linkFilmWithActor(int filmId, int actorId) throws SQLException {
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
