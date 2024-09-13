package org.example;

import org.example.entity.Actor;
import org.example.entity.Film;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Actor> actors = new ArrayList<>();
        Film film = new Film(1, "test",1, actors);
        System.out.println(film);
    }
}
