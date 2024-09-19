package org.example.service;

import org.example.dao.repository.ActorDAO;
import org.example.dao.repository.DAO;
import org.example.model.Actor;

import java.sql.SQLException;
import java.util.List;

public class ActorServiceImpl implements ActorService {

    private final DAO<Actor> dao = new ActorDAO();

    @Override
    public void create(Actor actor) throws SQLException {
        dao.create(actor);
    }

    @Override
    public void update(Actor actor) throws SQLException {
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
}
