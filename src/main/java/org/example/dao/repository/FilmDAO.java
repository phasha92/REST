package org.example.dao.repository;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.query.FilmQuery;
import org.example.model.Actor;
import org.example.model.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO {

    // Создание нового фильма
    public void createFilm(Film film) throws SQLException {

        String query = FilmQuery.CREATE.getQuery();

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getReleaseYear());
            statement.executeUpdate();

            // Получаем сгенерированный ID фильма
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    film.setId(generatedKeys.getInt(1));
                }
            }

            // Добавляем связи фильм-актер в таблицу Film_Actor
            if (film.getActors() != null) {
                addActorsToFilm(film.getId(), film.getActors());
            }
        }
    }

    // Получение фильма по ID
    public Film getFilmById(int id) throws SQLException {

        String query = FilmQuery.GET_BY_ID.getQuery();
        Film film = null;

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                List<Actor> actors = getActorsByFilmId(id);  // Получаем актеров для фильма

                film = new Film(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getInt("release_year"), actors);
            }
        }
        return film;
    }

    // Получение всех фильмов
    public List<Film> getAllFilms() throws SQLException {

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
    public void updateFilm(Film film) throws SQLException {

        String query = FilmQuery.UPDATE.getQuery();

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getReleaseYear());
            statement.setInt(3, film.getId());
            statement.executeUpdate();

            // Обновляем связи фильм-актер в таблице Film_Actor
            removeActorsFromFilm(film.getId());  // Удаляем старые связи
            addActorsToFilm(film.getId(), film.getActors());  // Добавляем новые связи
        }
    }

    // Удаление фильма по ID
    public void deleteFilm(int id) throws SQLException {

        String query = FilmQuery.DELETE.getQuery();

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
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

    // Добавление актеров к фильму
    private void addActorsToFilm(int filmId, List<Actor> actors) throws SQLException {

        String query = FilmQuery.ADD_ACTOR_TO_FILM.getQuery();

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (Actor actor : actors) {
                statement.setInt(1, filmId);
                statement.setInt(2, actor.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    // Удаление всех актеров из фильма
    private void removeActorsFromFilm(int filmId) throws SQLException {

        String query = FilmQuery.REMOVE_ACTORS_FROM_FILM.getQuery();

        try (Connection connection = DBConnectManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, filmId);
            statement.executeUpdate();
        }
    }
}
