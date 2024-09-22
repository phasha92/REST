package org.example.service;

import org.example.dao.repository.DirectorDAO;
import org.example.dao.repository.util.link.LinkDirectorWithFilm;
import org.example.model.Director;
import org.example.servlet.dto.ActorDTO;
import org.example.servlet.dto.DirectorDTO;
import org.example.servlet.mapper.DirectorMapper;
import org.example.servlet.mapper.Mapper;

import java.sql.SQLException;
import java.util.List;

public class DirectorServiceImpl implements DirectorService {

    private final DirectorDAO dao;
    private final Mapper mapper;
    private final LinkDirectorWithFilm directorWithFilm;

    public DirectorServiceImpl(DirectorDAO dao, Mapper mapper, LinkDirectorWithFilm directorWithFilm) {
        this.dao = dao;
        this.mapper = mapper;
        this.directorWithFilm = directorWithFilm;
    }

    public DirectorServiceImpl() {
        this.dao = new DirectorDAO();
        this.mapper = new DirectorMapper();  // Предполагаем наличие DirectorMapper
        this.directorWithFilm = new LinkDirectorWithFilm();
    }

    @Override
    public void create(DirectorDTO directorDTO) throws SQLException {
        Director director = (Director) mapper.toEntity(directorDTO);
        dao.create(director);
    }

    @Override
    public void update(DirectorDTO directorDTO) throws SQLException {
        Director director = (Director) mapper.toEntity(directorDTO);
        dao.update(director);
    }

    @Override
    public void delete(int id) throws SQLException {
        dao.delete(id);
    }

    @Override
    public Director getById(int id) throws SQLException {
        return dao.getById(id);
    }

    @Override
    public List<Director> getAll() throws SQLException {
        return dao.getAll();
    }

    @Override
    public void addDirectorToFilm(int filmId, int directorId) throws SQLException {
        directorWithFilm.linkFilmWithDirector(filmId, directorId);  
    }
}
