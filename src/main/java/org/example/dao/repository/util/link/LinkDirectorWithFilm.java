package org.example.dao.repository.util.link;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.query.LinkedQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkDirectorWithFilm {

    private final DBConnectManager connectManager;

    public LinkDirectorWithFilm(DBConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    public LinkDirectorWithFilm() {
        this.connectManager = new DBConnectManager();
    }


    public void linkEntities( int filmId, int directorID) throws SQLException {

        String existLinkQuery = LinkedQuery.EXIST_DIRECTOR_LINK.getQuery();
        String insertQuery = LinkedQuery.INSERT_DIRECTOR_LINK.getQuery();

        try (Connection connection = connectManager.getConnection();
             PreparedStatement eStatement = connection.prepareStatement(existLinkQuery);
             PreparedStatement iStatement = connection.prepareStatement(insertQuery)) {

            eStatement.setInt(1, directorID);
            eStatement.setInt(2, filmId);
            try (ResultSet res = eStatement.executeQuery()) {
                if (res.next() && res.getInt(1) > 0) {
                    throw new SQLException("Film id(" + directorID + ") and director id(" + filmId + ") are already linked");
                }

                iStatement.setInt(1, directorID);
                iStatement.setInt(2, filmId);
                iStatement.executeUpdate();
            }
        }
    }

    public void linkFilmWithDirector(int filmId, int directorId) throws SQLException {

        linkEntities(filmId, directorId);
    }
}
