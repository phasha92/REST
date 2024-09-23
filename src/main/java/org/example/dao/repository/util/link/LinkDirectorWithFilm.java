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
        checkEntityExists("Film", filmId);
        checkEntityExists("Director", directorId);

        linkEntities(filmId, directorId);
    }

    private void checkEntityExists(String entity, int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + entity + " WHERE id = ?";
        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    throw new SQLException(entity + " with id " + id + " does not exist.");
                }
            }
        }
    }
}
