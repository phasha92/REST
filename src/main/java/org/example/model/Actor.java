package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Actor extends Entity {
    private String name;
    private List<Film> films;  // Список фильмов

    public Actor(int id, String name, List<Film> films) {
        super(id);
        this.name = name;
        this.films = new ArrayList<>(films);  // Копируем список при создании объекта
    }

    public Actor() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return id == actor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, films);
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", films=" + films +
                '}';
    }
}
