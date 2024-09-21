package org.example.dao.repository;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.query.ActorQuery;
import org.example.model.Actor;
import org.example.model.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorDAO implements DAO<Actor>, LinkActorWithFilm {

    @Override
    public void create(Actor actor) throws SQLException {

        String query = ActorQuery.CREATE.getQuery();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectManager.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            connection.setAutoCommit(false);
            statement.setString(1, actor.getName());
            statement.executeUpdate();

            // Получаем сгенерированный ID актера
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    actor.setId(generatedKeys.getInt(1));  // Устанавливаем ID в объект Actor
                }
            }

            connection.commit(); // подтверждаем транзакцию/ или откат

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

    @Override
    public Actor getById(int id) throws SQLException {

        String query = ActorQuery.GET_BY_ID.getQuery();
        Actor actor = null;

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                List<Film> films = getFilmsByActorId(id); //Получаем все фильмы с актером

                actor = new Actor(id, name, films);
            }
        }
        return actor;
    }

    @Override
    public void update(Actor actor) throws SQLException {

        String query = ActorQuery.UPDATE.getQuery();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectManager.getConnection();
            statement = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            statement.setString(1, actor.getName());
            statement.setInt(2, actor.getId());
            statement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            throw e;
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public void delete(int id) throws SQLException {

        String query = ActorQuery.DELETE.getQuery();
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
                if (connection != null) connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            throw e;
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }


    // Получение всех актеров
    @Override
    public List<Actor> getAll() throws SQLException {

        String query = ActorQuery.GET_ALL.getQuery();
        List<Actor> actors = new ArrayList<>();

        try (Connection connection = DBConnectManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                List<Film> films = getFilmsByActorId(id);

                actors.add(new Actor(id, name, films));
            }
        }
        return actors;
    }

    // Получение фильмов для актера по его ID
    private List<Film> getFilmsByActorId(int actorId) throws SQLException {

        String query = ActorQuery.GET_FILMS_BY_ACTOR_ID.getQuery();
        List<Film> films = new ArrayList<>();

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, actorId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int filmId = resultSet.getInt("id");
                String filmTitle = resultSet.getString("title");
                int releaseYear = resultSet.getInt("release_year");

                films.add(new Film(filmId, filmTitle, releaseYear, new ArrayList<>()));
            }
        }
        return films;
    }

}


