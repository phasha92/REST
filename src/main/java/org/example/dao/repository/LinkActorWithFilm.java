package org.example.dao.repository;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.query.LinkedQuery;
import org.example.dao.repository.util.EntityExistenceChecker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkActorWithFilm {

    private final DBConnectManager connectManager;

    public LinkActorWithFilm(DBConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    public LinkActorWithFilm() {
        this.connectManager = new DBConnectManager();
    }

    public void linkEntities(int filmId, int actorId) throws SQLException {

        String existLinkQuery = LinkedQuery.EXIST_LINK.getQuery();
        String insertQuery = LinkedQuery.INSERT_LINK.getQuery();

        try (Connection connection = connectManager.getConnection();
             PreparedStatement eStatement = connection.prepareStatement(existLinkQuery);
             PreparedStatement iStatement = connection.prepareStatement(insertQuery)) {

            // Проверка, существует ли уже такая связь
            eStatement.setInt(1, filmId);
            eStatement.setInt(2, actorId);
            try (ResultSet res = eStatement.executeQuery()) {
                if (res.next() && res.getInt(1) > 0) {
                    throw new SQLException("Film id(" + filmId + ") and actor id(" + actorId + ") are already linked");
                }

                // Если связи нет, то добавляем её
                iStatement.setInt(1, filmId);
                iStatement.setInt(2, actorId);
                iStatement.executeUpdate();
            }
        }
    }

    public void linkFilmWithActor(int filmId, int actorId) throws SQLException {

        EntityExistenceChecker checker = new EntityExistenceChecker();
        // Проверка существования фильма и актёра
        if (!checker.isExist("Film", filmId)) {
            throw new SQLException("Film with id " + filmId + " does not exist.");
        }

        if (!checker.isExist("Actor", actorId)) {
            throw new SQLException("Actor with id " + actorId + " does not exist.");
        }

        // Связываем фильм с актёром через таблицу Film_Actor
        linkEntities(filmId, actorId);
    }
}
