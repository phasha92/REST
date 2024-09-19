package org.example.service;

import org.example.model.Actor;

import java.sql.SQLException;

public interface ActorService extends EntityService<Actor> {
    void create(Actor actor) throws SQLException;

    void update(Actor actor) throws SQLException;

    void delete(int id) throws SQLException;

    void linkActorWithFilm(int filmId, int actorId) throws SQLException;
}
