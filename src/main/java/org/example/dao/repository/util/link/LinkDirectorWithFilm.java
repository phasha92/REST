package org.example.dao.repository.util.link;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.query.LinkedQuery;
import org.example.dao.repository.util.EntityExistenceChecker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkDirectorWithFilm {

    private final DBConnectManager connectManager;
    private final EntityExistenceChecker entityExistenceChecker;

    public LinkDirectorWithFilm(DBConnectManager connectManager, EntityExistenceChecker entityExistenceChecker) {
        this.connectManager = connectManager;
        this.entityExistenceChecker = entityExistenceChecker;
    }

    public LinkDirectorWithFilm() {
        this.connectManager = new DBConnectManager();
        this.entityExistenceChecker = new EntityExistenceChecker();
    }

    public void linkEntities(int filmId, int directorID) throws SQLException {
        String existLinkQuery = LinkedQuery.EXIST_DIRECTOR_LINK.getQuery();
        String insertQuery = LinkedQuery.INSERT_DIRECTOR_LINK.getQuery();

        try (Connection connection = connectManager.getConnection();
             PreparedStatement eStatement = connection.prepareStatement(existLinkQuery);
             PreparedStatement iStatement = connection.prepareStatement(insertQuery)) {

            // Проверка, существует ли уже такая связь
            eStatement.setInt(1, filmId);
            eStatement.setInt(2, directorID);
            try (ResultSet res = eStatement.executeQuery()) {
                if (res.next() && res.getInt(1) > 0) {
                    throw new SQLException("Film id(" + filmId + ") and director id(" + directorID + ") are already linked");
                }

                // Если связи нет, то обновляем режиссера
                iStatement.setInt(1, directorID);
                iStatement.setInt(2, filmId);
                iStatement.executeUpdate();
            }
        }
    }


    public void linkFilmWithDirector(int filmId, int directorId) throws SQLException {
        // Добавьте проверки существования для фильма и режиссера

        if (!entityExistenceChecker.isExist("Film", filmId)) throw new SQLException("Film with id " + filmId + " does not exist.");
        if (!entityExistenceChecker.isExist("Director", directorId)) throw new SQLException("Director with id " + directorId + " does not exist.");

        linkEntities(filmId, directorId);
    }

}
