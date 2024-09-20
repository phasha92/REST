package org.example.dao.repository.query;

public enum ActorQuery {
    CREATE("INSERT INTO Actor (name) VALUES (?)"),
    GET_BY_ID("SELECT * FROM Actor WHERE id = ?"),
    GET_ALL("SELECT * FROM Actor"),
    UPDATE("UPDATE Actor SET name = ? WHERE id = ?"),
    DELETE("DELETE FROM Actor WHERE id = ?"),
    GET_FILMS_BY_ACTOR_ID("SELECT f.id, f.title, f.release_year FROM Film f " +
            "JOIN Film_Actor fa ON f.id = fa.film_id WHERE fa.actor_id = ?");

    private final String query;

    ActorQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
