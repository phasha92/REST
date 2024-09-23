package org.example.dao.repository;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.query.FilmQuery;
import org.example.model.Actor;
import org.example.model.Director;
import org.example.model.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO implements DAO<Film> {

    private final DBConnectManager connectManager;

    public FilmDAO() {
        this.connectManager = new DBConnectManager();
    }

    public FilmDAO(DBConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    @Override
    public void create(Film film) throws SQLException {

        String query = FilmQuery.CREATE.getQuery();

        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getReleaseYear());
            statement.setInt(3, film.getDirector().getId());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    film.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public Film getById(int id) throws SQLException {

        String query = FilmQuery.GET_BY_ID.getQuery();
        Film film = null;

        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                int releaseYear = resultSet.getInt("release_year");
                int directorId = resultSet.getInt("director_id");
                Director director = getDirectorById(directorId);  // Получаем режиссера
                List<Actor> actors = getActorsByFilmId(id);  // Получаем актеров для фильма

                film = new Film(id, title, releaseYear, actors, director);
            }
        }
        return film;
    }

    @Override
    public List<Film> getAll() throws SQLException {

        String query = FilmQuery.GET_ALL.getQuery();
        List<Film> films = new ArrayList<>();

        try (Connection connection = connectManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int releaseYear = resultSet.getInt("release_year");
                int directorId = resultSet.getInt("director_id");  // Получаем id режиссера

                List<Actor> actors = getActorsByFilmId(id);  // Получаем актеров для фильма

                Director director = getDirectorById(directorId);

                films.add(new Film(id, title, releaseYear, actors, director));
            }
        }
        return films;
    }

    @Override
    public void update(Film film) throws SQLException {

        String query = FilmQuery.UPDATE.getQuery();
        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getReleaseYear());
            statement.setInt(3, film.getDirector().getId());
            statement.setInt(4, film.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {

        String query = FilmQuery.DELETE.getQuery();

        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // Получение актеров для фильма по его ID
    public List<Actor> getActorsByFilmId(int filmId) throws SQLException {

        String query = FilmQuery.GET_ACTORS_BY_FILM_ID.getQuery();
        List<Actor> actors = new ArrayList<>();

        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, filmId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                actors.add(new Actor(id, name, new ArrayList<>()));
            }
        }
        return actors;
    }

    public Director getDirectorById(int directorId) throws SQLException {
        String query = FilmQuery.GET_DIRECTOR_BY_FILM_ID.getQuery();

        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, directorId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                return new Director(directorId, name, List.of());
            }
        }
        return null;
    }

    public List<Film> getFilmsByDirectorId(int directorId) throws SQLException {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film WHERE director_id = ?";
        try (Connection connection = connectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, directorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Director director = new Director();
                    director.setId(resultSet.getInt("director_id"));
                    director.setName("");
                    films.add(new Film(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getInt("release_year"),
                            List.of(),
                            director // Пустое имя, так как мы его получаем через JOIN
                    ));
                }
            }
        }
        return films;
    }

}
