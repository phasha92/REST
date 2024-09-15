package org.example.dao.repository;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.query.ActorQuery;
import org.example.model.Actor;
import org.example.model.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorDAO {

    // Создание нового актера
    public void createActor(Actor actor) throws SQLException {
        String query = ActorQuery.CREATE.getQuery();

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, actor.getName());
            statement.executeUpdate();
        }
    }

    // Получение актера по ID
    public Actor getActorById(int id) throws SQLException {
        String query = ActorQuery.GET_BY_ID.getQuery();
        Actor actor = null;

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                List<Film> films = getFilmsByActorId(id);
                actor = new Actor(resultSet.getInt("id"), resultSet.getString("name"), films);
            }
        }
        return actor;
    }

    // Получение всех актеров
    public List<Actor> getAllActors() throws SQLException {
        String query = ActorQuery.GET_ALL.getQuery();
        List<Actor> actors = new ArrayList<>();

        try (Connection connection = DBConnectManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                List<Film> films = getFilmsByActorId(id);
                Actor actor = new Actor(id, resultSet.getString("name"), films);
                actors.add(actor);
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
                films.add(new Film(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getInt("release_year"), new ArrayList<>()));
            }
        }
        return films;
    }
}


