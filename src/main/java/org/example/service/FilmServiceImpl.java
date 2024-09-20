package org.example.service;

import org.example.DAOTest.RepositoryTest.FilmDAO;
import org.example.model.Film;
import org.example.servlet.dto.FilmDTO;
import org.example.servlet.mapper.FilmMapper;
import org.example.servlet.mapper.Mapper;

import java.sql.SQLException;
import java.util.List;

public class FilmServiceImpl implements FilmService {

    private final FilmDAO dao = new FilmDAO();
    private final Mapper mapper = new FilmMapper();

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
        dao.linkFilmWithActor(filmId, actorId);  // Добавляем связь
    }

}
