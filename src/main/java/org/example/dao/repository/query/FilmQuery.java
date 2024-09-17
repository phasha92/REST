package org.example.dao.repository.query;

public enum FilmQuery {
    CREATE("INSERT INTO Film (title, release_year) VALUES (?, ?)"),
    GET_BY_ID("SELECT * FROM Film WHERE id = ?"),
    GET_ALL("SELECT * FROM Film"),
    UPDATE("UPDATE Film SET title = ?, release_year = ? WHERE id = ?"),
    DELETE("DELETE FROM Film WHERE id = ?"),
    GET_ACTORS_BY_FILM_ID("SELECT a.id, a.name FROM Actor a " +
            "JOIN Film_Actor fa ON a.id = fa.actor_id WHERE fa.film_id = ?");
   // ADD_ACTOR_TO_FILM("INSERT INTO Film_Actor (film_id, actor_id) VALUES (?, ?)"),
   // REMOVE_ACTORS_FROM_FILM("DELETE FROM Film_Actor WHERE film_id = ?");

    private final String query;

    FilmQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
