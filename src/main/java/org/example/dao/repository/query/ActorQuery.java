package org.example.dao.repository.query;

public enum ActorQuery {
    CREATE("INSERT INTO actor (name) VALUES (?)"),
    GET_BY_ID("SELECT * FROM actor WHERE id = ?"),
    GET_ALL("SELECT * FROM actor"),
    UPDATE("UPDATE actor SET name = ? WHERE id = ?"),
    DELETE("DELETE FROM actor WHERE id = ?"),
    GET_FILMS_BY_ACTOR_ID("SELECT f.id, f.title, f.release_year, f.director_id FROM film f JOIN film_actor fa ON f.id = fa.film_id WHERE fa.actor_id = ?");

    private final String query;

    ActorQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}