package org.example.model;

public abstract class Entity {
    protected int id;
    Entity(){};
    Entity(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
