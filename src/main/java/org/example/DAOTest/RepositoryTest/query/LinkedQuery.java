package org.example.DAOTest.RepositoryTest.query;

public enum LinkedQuery {
    EXIST_LINK("SELECT COUNT(*) FROM Film_Actor WHERE film_id = ? AND actor_id = ?"),
    INSERT_LINK("INSERT INTO Film_Actor (film_id, actor_id) VALUES (?, ?)");

    private final String query;

    LinkedQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
