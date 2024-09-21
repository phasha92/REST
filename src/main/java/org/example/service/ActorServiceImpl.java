package org.example.service;

import org.example.dao.repository.ActorDAO;
import org.example.dao.repository.LinkActorWithFilm;
import org.example.model.Actor;

import org.example.servlet.dto.ActorDTO;
import org.example.servlet.mapper.ActorMapper;
import org.example.servlet.mapper.Mapper;

import java.sql.SQLException;
import java.util.List;

public class ActorServiceImpl implements ActorService {

    private final ActorDAO dao = new ActorDAO();
    private final Mapper mapper = new ActorMapper();

    @Override
    public void create(ActorDTO actorDTO) throws SQLException {
        Actor actor = (Actor) mapper.toEntity(actorDTO);
        dao.create(actor);
    }

    @Override
    public void update(ActorDTO actorDTO) throws SQLException {
        Actor actor = (Actor) mapper.toEntity(actorDTO);
        dao.update(actor);
    }

    @Override
    public void delete(int id) throws SQLException {
        dao.delete(id);
    }

    @Override
    public Actor getById(int id) throws SQLException {
        return dao.getById(id);
    }

    @Override
    public List<Actor> getAll() throws SQLException {
        return dao.getAll();
    }

    @Override
    public void addFilmToActor(int actorId, int filmId) throws SQLException {
        new LinkActorWithFilm().linkFilmWithActor(filmId, actorId);  // Добавляем связь актёра и фильма
    }
}
