package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Film {
    private int id;
    private String title;
    private int releaseYear;
    private List<Actor> actors;  // Список актеров

    public Film(int id, String title, int releaseYear, List<Actor> actors) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.actors = new ArrayList<>(actors);  // Копируем список при создании объекта
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        this.actors = actors;
    }
}
