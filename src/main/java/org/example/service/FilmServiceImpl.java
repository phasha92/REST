package org.example.service;

import org.example.dao.repository.DAO;
import org.example.dao.repository.FilmDAO;
import org.example.model.Film;

import java.sql.SQLException;
import java.util.List;

public class FilmServiceImpl implements FilmService {

    private DAO<Film> dao = new FilmDAO();

    @Override
    public void create(Film film) throws SQLException {
        dao.create(film);
    }

    @Override
    public void update(Film film) throws SQLException {
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

}
