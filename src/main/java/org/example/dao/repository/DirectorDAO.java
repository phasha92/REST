package org.example.dao.repository;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.query.DirectorQuery;
import org.example.model.Director;
import org.example.model.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DirectorDAO implements DAO<Director> {

    private final DBConnectManager connectManager;

    public DirectorDAO(DBConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    public DirectorDAO() {
        this.connectManager = new DBConnectManager();
    }

    @Override
    public void create(Director director) throws SQLException {
        String query = DirectorQuery.CREATE.getQuery();
        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, director.getName());
            statement.executeUpdate();

            // Получаем сгенерированный ID режиссера
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    director.setId(generatedKeys.getInt(1)); // Устанавливаем ID в объект Director
                }
            }
        }
    }

    @Override
    public Director getById(int id) throws SQLException {
        String query = DirectorQuery.GET_BY_ID.getQuery();
        Director director = new Director();
        director.setName("");
        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                director = new Director(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        new ArrayList<>());
            }
        }
        return director;
    }

    @Override
    public void update(Director director) throws SQLException {
        String query = DirectorQuery.UPDATE.getQuery();

        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, director.getName());
            statement.setInt(2, director.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = DirectorQuery.DELETE.getQuery();

        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<Film> getFilmsByDirectorId(int directorId) throws SQLException {
        String query = DirectorQuery.GET_FILMS_BY_ID.getQuery();
        List<Film> films = new ArrayList<>();

        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, directorId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int filmId = resultSet.getInt("id");
                String filmTitle = resultSet.getString("title");
                int releaseYear = resultSet.getInt("release_year");

                Director director = this.getById(directorId);

                films.add(new Film(filmId, filmTitle, releaseYear, new ArrayList<>(), director));
            }
        }
        return films;
    }


    public List<Director> getAll() throws SQLException {
        List<Director> directors = new ArrayList<>();
        String query = DirectorQuery.GET_ALL.getQuery();

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
