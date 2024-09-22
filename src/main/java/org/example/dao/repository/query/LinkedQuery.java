package org.example.dao.repository.query;

public enum LinkedQuery {
    EXIST_LINK("SELECT COUNT(*) FROM film_actor WHERE film_id = ? AND actor_id = ?"),
    INSERT_LINK("INSERT INTO film_actor (film_id, actor_id) VALUES (?, ?)"),
    EXIST_DIRECTOR_LINK("SELECT COUNT(*) FROM film WHERE id = ? AND director_id = ?"),
    INSERT_DIRECTOR_LINK("UPDATE film SET director_id = ? WHERE id = ?");

    private final String query;

    LinkedQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}