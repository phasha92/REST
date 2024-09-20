package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Film extends Entity {
    private String title;
    private int releaseYear;
    private List<Actor> actors = new ArrayList<>();  // Список актеров

    public Film(int id, String title, int releaseYear, List<Actor> actors) {
        super(id);
        this.title = title;
        this.releaseYear = releaseYear;
        this.actors = actors != null ? new ArrayList<>(actors) : new ArrayList<>();
    }

    public Film() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    // Возвращаем неизменяемый список
    public List<Actor> getActors() {
        return Collections.unmodifiableList(actors);  // Возвращаем неизменяемый список
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors != null ? actors : new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                ", actors=" + actors +
                '}';
    }
}
