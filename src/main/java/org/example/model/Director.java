package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Director extends Entity {

    private String name;
    private List<Film> films;

    public Director(int id, String name, List<Film> films) {
        super(id);
        this.name = name;
        this.films = films != null ? films : new ArrayList<>();
    }

    public Director() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Film> getFilms() {
        return Collections.unmodifiableList(films);
    }

    public void setFilms(List<Film> films) {
        this.films = films != null ? films : new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Director director = (Director) o;
        return id == director.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Director{" +
                "name='" + name + '\'' +
                ", films=" + films +
                ", id=" + id +
                '}';
    }
}
