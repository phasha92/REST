package org.example.dao.repository.util.link;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.query.LinkedQuery;

import java.sql.SQLException;

import org.example.dao.repository.util.EntityExistenceChecker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class LinkActorWithFilm {

    private final DBConnectManager connectManager;
    private final EntityExistenceChecker entityExistenceChecker;

    public LinkActorWithFilm(DBConnectManager connectManager, EntityExistenceChecker entityExistenceChecker) {
        this.connectManager = connectManager;
        this.entityExistenceChecker = entityExistenceChecker;
    }

    public LinkActorWithFilm() {
        this.connectManager = new DBConnectManager();
        this.entityExistenceChecker = new EntityExistenceChecker();
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

                iStatement.setInt(1, filmId);
                iStatement.setInt(2, actorId);
                iStatement.executeUpdate();
            }
        }
    }

    public void linkFilmWithActor(int filmId, int actorId) throws SQLException {

        EntityExistenceChecker checker = entityExistenceChecker;

        if (!checker.isExist("Film", filmId)) {
            throw new SQLException("Film with id " + filmId + " does not exist.");
        }

        if (!checker.isExist("Actor", actorId)) {
            throw new SQLException("Actor with id " + actorId + " does not exist.");
        }

        linkEntities(filmId, actorId);
    }
}