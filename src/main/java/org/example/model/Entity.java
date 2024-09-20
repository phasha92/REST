package org.example.model;

public abstract class Entity {
    protected int id;

    Entity() {
    }

    ;

    Entity(int id) {
        this.setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("Id must be positive");
        this.id = id;
    }
}
