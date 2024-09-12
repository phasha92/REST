package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Actor {
    private int id;
    private String name;
    private List<Film> films;  // Список фильмов

    public Actor(int id, String name, List<Film> films) {
        this.id = id;
        this.name = name;
        this.films = new ArrayList<>(films);  // Копируем список при создании объекта
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }

    // Возвращаем неизменяемый список
    public List<Film> getFilms() {
        return Collections.unmodifiableList(films);  // Возвращаем неизменяемый список
    }

}
