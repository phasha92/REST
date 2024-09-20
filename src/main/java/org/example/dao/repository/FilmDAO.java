package org.example.dao.repository;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.query.FilmQuery;
import org.example.model.Actor;
import org.example.model.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO implements DAO<Film>,LinkActorWithFilm {

    // Создание нового фильма
    @Override
    public void create(Film film) throws SQLException {

        String query = FilmQuery.CREATE.getQuery();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectManager.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            connection.setAutoCommit(false);
            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getReleaseYear());
            statement.executeUpdate();

            // Получаем сгенерированный ID фильма
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    film.setId(generatedKeys.getInt(1));
                }
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();  // Откат транзакции в случае ошибки
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            throw e;  // Пробрасываем исключение дальше
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    // Получение фильма по ID
    @Override
    public Film getById(int id) throws SQLException {

        String query = FilmQuery.GET_BY_ID.getQuery();
        Film film = null;

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                String title = resultSet.getString("title");
                int releaseYear = resultSet.getInt("release_year");
                List<Actor> actors = getActorsByFilmId(id);  // Получаем актеров для фильма

                film = new Film(id, title, releaseYear, actors);
            }
        }
        return film;
    }

    // Получение всех фильмов
    @Override
    public List<Film> getAll() throws SQLException {

        String query = FilmQuery.GET_ALL.getQuery();
        List<Film> films = new ArrayList<>();

        try (Connection connection = DBConnectManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int releaseYear = resultSet.getInt("release_year");
                List<Actor> actors = getActorsByFilmId(id);  // Получаем актеров для фильма

                films.add(new Film(id, title, releaseYear, actors));
            }
        }
        return films;
    }

    // Обновление фильма
    @Override
    public void update(Film film) throws SQLException {

        String query = FilmQuery.UPDATE.getQuery();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectManager.getConnection();
            statement = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getReleaseYear());
            statement.setInt(3, film.getId());
            statement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();  // Откат транзакции в случае ошибки
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            throw e;  // Пробрасываем исключение дальше
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    // Удаление фильма по ID
    @Override
    public void delete(int id) throws SQLException {

        String query = FilmQuery.DELETE.getQuery();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectManager.getConnection();
            statement = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();  // Откат транзакции в случае ошибки
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            throw e;  // Пробрасываем исключение дальше
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    // Получение актеров для фильма по его ID
    private List<Actor> getActorsByFilmId(int filmId) throws SQLException {

        String query = FilmQuery.GET_ACTORS_BY_FILM_ID.getQuery();
        List<Actor> actors = new ArrayList<>();

        try (Connection connection = DBConnectManager.getConnection();
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

}
