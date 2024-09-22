package org.example.dao.repository.query;

public enum FilmQuery {
    CREATE("INSERT INTO film (title, release_year, director_id) VALUES (?, ?, ?)"),
    GET_BY_ID("SELECT f.*, d.name AS director_name FROM film f LEFT JOIN director d ON f.director_id = d.id WHERE f.id = ?"),
    GET_ALL("SELECT f.*, d.name AS director_name FROM film f LEFT JOIN director d ON f.director_id = d.id"),
    UPDATE("UPDATE film SET title = ?, release_year = ?, director_id = ? WHERE id = ?"),
    DELETE("DELETE FROM film WHERE id = ?"),
    GET_ACTORS_BY_FILM_ID("SELECT a.id, a.name FROM actor a JOIN film_actor fa ON a.id = fa.actor_id WHERE fa.film_id = ?"),
    GET_DIRECTOR_BY_FILM_ID("SELECT * FROM director WHERE id = ?");

    private final String query;

    FilmQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}