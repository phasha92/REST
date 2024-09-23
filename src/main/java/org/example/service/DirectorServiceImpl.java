package org.example.service;

import org.example.dao.repository.DirectorDAO;
import org.example.dao.repository.util.link.LinkDirectorWithFilm;
import org.example.model.Director;
import org.example.servlet.dto.DirectorDTO;
import org.example.servlet.mapper.DirectorMapper;
import org.example.servlet.mapper.Mapper;

import java.sql.SQLException;
import java.util.List;

public class DirectorServiceImpl implements DirectorService {

    private final DirectorDAO directorDAO;
    private final Mapper mapper;
    private final LinkDirectorWithFilm directorWithFilm;

    public DirectorServiceImpl(DirectorDAO directorDAO, Mapper mapper, LinkDirectorWithFilm directorWithFilm) {
        this.directorDAO = directorDAO;
        this.mapper = mapper;
        this.directorWithFilm = directorWithFilm;
    }

    public DirectorServiceImpl() {
        this.directorDAO = new DirectorDAO();
        this.mapper = new DirectorMapper();
        this.directorWithFilm = new LinkDirectorWithFilm();
    }

    public DirectorDAO getDirectorDAO() {
        return directorDAO;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public LinkDirectorWithFilm getDirectorWithFilm() {
        return directorWithFilm;
    }

    @Override
    public void create(DirectorDTO directorDTO) throws SQLException {
        Director director = (Director) mapper.toEntity(directorDTO);
        directorDAO.create(director);
    }

    @Override
    public void update(DirectorDTO directorDTO) throws SQLException {
        Director director = (Director) mapper.toEntity(directorDTO);
        directorDAO.update(director);
    }

    @Override
    public void delete(int id) throws SQLException {
        directorDAO.delete(id);
    }

    @Override
    public Director getById(int id) throws SQLException {
        return directorDAO.getById(id);
    }

    @Override
    public List<Director> getAll() throws SQLException {
        return directorDAO.getAll();
    }

    @Override
    public void addDirectorToFilm(int filmId, int directorId) throws SQLException {
        directorWithFilm.linkFilmWithDirector(filmId, directorId);
    }
}
