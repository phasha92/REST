package org.example.dao.repository.query;

public enum FilmQuery {
    CREATE("INSERT INTO Film (title, release_year, director_id) VALUES (?, ?, ?)"),
    GET_BY_ID("SELECT f.*, d.name AS director_name FROM Film f LEFT JOIN Director d ON f.director_id = d.id WHERE f.id = ?"),
    GET_ALL("SELECT f.*, d.name AS director_name FROM Film f LEFT JOIN Director d ON f.director_id = d.id"),
    UPDATE("UPDATE Film SET title = ?, release_year = ?, director_id = ? WHERE id = ?"),
    DELETE("DELETE FROM Film WHERE id = ?"),
    GET_ACTORS_BY_FILM_ID("SELECT a.id, a.name FROM Actor a " +
            "JOIN Film_Actor fa ON a.id = fa.actor_id WHERE fa.film_id = ?"),
    GET_DIRECTOR_BY_FILM_ID("SELECT * FROM director WHERE id = ?");

    private final String query;

    FilmQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
