package org.example.service;

import org.example.model.Actor;
import org.example.servlet.dto.ActorDTO;

import java.sql.SQLException;

public interface ActorService extends EntityService<Actor> {
    void create(ActorDTO actorDTO) throws SQLException;

    void update(ActorDTO actorDTO) throws SQLException;

    void delete(int id) throws SQLException;

    void addFilmToActor(int actorId, int filmId) throws SQLException;
}
