package org.example.dao.repository.query;

public enum DirectorQuery {
    CREATE("INSERT INTO director (name) VALUES (?) RETURNING id"),
    GET_BY_ID("SELECT * FROM director WHERE id = ?"),
    UPDATE("UPDATE director SET name = ? WHERE id = ?"),
    DELETE("DELETE FROM director WHERE id = ?"),
    GET_FILMS_BY_ID("SELECT * FROM film WHERE director_id = ?"),
    GET_ALL("SELECT * FROM director");

    private final String query;

    DirectorQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}