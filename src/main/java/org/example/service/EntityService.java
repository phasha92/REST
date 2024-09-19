package org.example.service;

import java.sql.SQLException;
import java.util.List;

public interface EntityService<T> {
    T getById(int id) throws SQLException;

    List<T> getAll() throws SQLException;

}
