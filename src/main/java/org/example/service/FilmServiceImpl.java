package org.example.service;

import org.example.dao.repository.FilmDAO;
import org.example.dao.repository.util.link.LinkActorWithFilm;
import org.example.dao.repository.util.link.LinkDirectorWithFilm;
import org.example.model.Film;
import org.example.servlet.dto.FilmDTO;
import org.example.servlet.mapper.FilmMapper;
import org.example.servlet.mapper.Mapper;

import java.sql.SQLException;
import java.util.List;

public class FilmServiceImpl implements FilmService {

    private final FilmDAO dao;
    private final Mapper mapper;
    private final LinkActorWithFilm actorWithFilm;
    private final LinkDirectorWithFilm directorWithFilm;


    public FilmServiceImpl(FilmDAO dao, Mapper mapper, LinkActorWithFilm actorWithFilm, LinkDirectorWithFilm directorWithFilm) {
        this.dao = dao;
        this.mapper = mapper;
        this.actorWithFilm = actorWithFilm;
        this.directorWithFilm = directorWithFilm;
    }

    public FilmServiceImpl() {
        this.dao = new FilmDAO();
        this.mapper = new FilmMapper();
        this.actorWithFilm = new LinkActorWithFilm();
        this.directorWithFilm = new LinkDirectorWithFilm();
    }

    public FilmDAO getDao() {
        return dao;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public LinkActorWithFilm getActorWithFilm() {
        return actorWithFilm;
    }

    @Override
    public void create(FilmDTO filmDTO) throws SQLException {
        Film film = (Film) mapper.toEntity(filmDTO);
        dao.create(film);
    }

    @Override
    public void update(FilmDTO filmDTO) throws SQLException {
        Film film = (Film) mapper.toEntity(filmDTO);
        dao.update(film);
    }

    @Override
    public void delete(int id) throws SQLException {
        dao.delete(id);
    }

    @Override
    public Film getById(int id) throws SQLException {
        return dao.getById(id);
    }

    @Override
    public List<Film> getAll() throws SQLException {
        return dao.getAll();
    }

    @Override
    public void addActorToFilm(int filmId, int actorId) throws SQLException {
        actorWithFilm.linkFilmWithActor(filmId, actorId);
    }

    @Override
    public void addDirectorToFilm(int filmId, int directorId) throws SQLException {
        directorWithFilm.linkFilmWithDirector(filmId, directorId);
    }
}
