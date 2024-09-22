package org.example.service;

import org.example.model.Director;
import org.example.servlet.dto.DirectorDTO;

import java.sql.SQLException;

public interface DirectorService extends EntityService<Director> {
    void create(DirectorDTO directorDTO) throws SQLException;

    void update(DirectorDTO directorDTO) throws SQLException;

    void delete(int id) throws SQLException;

    void addDirectorToFilm(int filmId, int directorId) throws SQLException;
}
