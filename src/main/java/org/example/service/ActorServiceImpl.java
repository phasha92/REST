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

    private final ActorDAO dao;
    private final Mapper mapper;
    private final LinkActorWithFilm actorWithFilm;

    public ActorServiceImpl(ActorDAO dao, Mapper mapper, LinkActorWithFilm actorWithFilm) {
        this.dao = dao;
        this.mapper = mapper;
        this.actorWithFilm = actorWithFilm;
    }

    public ActorServiceImpl() {
        this.dao = new ActorDAO();
        this.mapper = new ActorMapper();
        this.actorWithFilm = new LinkActorWithFilm();
    }

    public ActorDAO getDao() {
        return dao;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public LinkActorWithFilm getActorWithFilm(){
        return actorWithFilm;
    }


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
        actorWithFilm.linkFilmWithActor(filmId, actorId);  // Добавляем связь актёра и фильма
    }
}
