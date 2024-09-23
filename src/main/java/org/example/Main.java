package org.example;

import com.google.gson.Gson;
import org.example.dao.repository.util.EntityExistenceChecker;
import org.example.dao.repository.util.link.LinkActorWithFilm;
import org.example.dao.repository.util.link.LinkDirectorWithFilm;
import org.example.model.Director;
import org.example.model.Film;
import org.example.servlet.mapper.FilmMapper;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Director director = new Director();
        director.setName("Director");
        Film film = new Film(1, "qqq", 2000, List.of(), null);
        System.out.println(new Gson().toJson(film));
        System.out.println(new Gson().toJson(new FilmMapper().toDTO(film)));
    }
}
