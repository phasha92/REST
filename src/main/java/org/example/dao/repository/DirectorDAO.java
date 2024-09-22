package org.example.dao.repository;

import org.example.dao.DBConnectManager;
import org.example.model.Director;
import org.example.model.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DirectorDAO {
    private final DBConnectManager connectManager;

    public DirectorDAO(DBConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    public void create(Director director) throws SQLException {
        String query = "INSERT INTO Director (name) VALUES (?) RETURNING id";
        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, director.getName());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                director.setId(resultSet.getInt("id"));
            }
        }
    }

    public Director getById(int id) throws SQLException {
        String query = "SELECT * FROM Director WHERE id = ?";
        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Director(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        new ArrayList<>());
            }
        }
        return null;
    }

    public void update(Director director) throws SQLException {
        String query = "UPDATE Director SET name = ? WHERE id = ?";
        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, director.getName());
            statement.setInt(2, director.getId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Director WHERE id = ?";
        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<Film> getFilmsByDirectorId(int directorId) throws SQLException {
        String query = "SELECT * FROM Film WHERE director_id = ?";
        List<Film> films = new ArrayList<>();

        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, directorId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int filmId = resultSet.getInt("id");
                String filmTitle = resultSet.getString("title");
                int releaseYear = resultSet.getInt("release_year");

                films.add(new Film(filmId, filmTitle, releaseYear, new ArrayList<>(), new Director(directorId, "", new ArrayList<>())));
            }
        }
        return films;
    }


    public List<Director> getAll() throws SQLException {
        List<Director> directors = new ArrayList<>();
        String query = "SELECT * FROM Director";

        try (Connection connection = connectManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                directors.add(new Director(resultSet.getInt("id"), resultSet.getString("name"), new ArrayList<>()));
            }
        }
        return directors;
    }
}
