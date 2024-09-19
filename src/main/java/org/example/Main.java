package org.example;

import org.example.dao.repository.ActorDAO;
import org.example.dao.repository.DAO;
import org.example.dao.repository.FilmDAO;
import org.example.dao.repository.util.EntityExistenceChecker;
import org.example.model.Actor;
import org.example.model.Entity;
import org.example.model.Film;

import org.example.service.*;
import org.example.servlet.ActorServlet;
import org.example.servlet.dto.ActorDTO;
import org.example.servlet.dto.DTO;
import org.example.servlet.mapper.ActorMapper;
import org.example.servlet.mapper.Mapper;
import org.example.servlet.mapper.factory.MapperFactoryImp;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
/*
        // Создание актеров
        Actor actor1 = new Actor();
        actor1.setName("111");
        Actor actor2 = new Actor();
        actor2.setName("222");
        Actor actor3 = new Actor();
        actor3.setName("333");
        Actor actor4 = new Actor();
        actor4.setName("444");

        List<Actor> actors = new ArrayList<>() {{
            add(actor1);
            add(actor2);
            add(actor3);
            add(actor4);
        }};

        // Создание фильма с актерами
        Entity film = new Film(1, "test", 1, actors);
        System.out.println(film);

        // Использование фабрики для получения маппера
        MapperFactory factory = new MapperFactoryImp();

        Mapper mapper = factory.getMapperForEntity(film);

        DTO newFilm = mapper.toDTO(film);
        System.out.println(newFilm);

        film = mapper.toEntity(newFilm);
        System.out.println(film);

        Entity finalFilm = film;
        List<Film> films = new ArrayList<>() {{
            add((Film) finalFilm);
        }};
        Entity actor = new Actor(1, "123", films);
        System.out.println(actor);

        mapper = factory.getMapperForEntity(actor);
        DTO actorDTO = mapper.toDTO(actor);
        System.out.println(actorDTO);
        mapper = factory.getMapperForDTO(actorDTO);
        System.out.println(mapper.toEntity(actorDTO));
*/
        /*
        ActorDAO actorDAO = new ActorDAO();

        try {
            // 1. Создание нового актера
            Actor actor = new Actor();
            actor.setName("Robert Downey Stark.");
            actorDAO.create(actor);
            System.out.println("Actor created: " + actor);

            // 2. Получение актера по ID
            Actor retrievedActor = actorDAO.getById(actor.getId());
            System.out.println("Actor retrieved: " + retrievedActor);

            // 3. Обновление актера
            actor.setName("Updated Name");
            actorDAO.update(actor);
            System.out.println("Actor updated: " + actorDAO.getById(actor.getId()));

            // 4. Удаление актера
            actorDAO.delete(actor.getId());
            System.out.println("Actor deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        FilmDAO filmDAO = new FilmDAO();

        try {
            // 1. Создание нового фильма
            Film film = new Film();
            film.setTitle("Iron Man");
            film.setReleaseYear(2008);
            filmDAO.create(film);
            System.out.println("Film created: " + film);

            // 2. Получение фильма по ID
            Film retrievedFilm = filmDAO.getById(film.getId());
            System.out.println("Film retrieved: " + retrievedFilm);

            // 3. Обновление фильма
            film.setTitle("Iron Man (Updated)");
            filmDAO.update(film);
            System.out.println("Film updated: " + filmDAO.getById(film.getId()));

            // 4. Удаление фильма

            filmDAO.delete(film.getId());
            System.out.println("Film deleted");

            new ActorDAO().getAll().forEach((actor) -> {

                Mapper mapper = new MapperFactoryImp().getMapperForEntity(actor);
                System.out.println(mapper.toDTO(actor));
            });

            filmDAO.getAll().forEach((film1) -> {
                Mapper mapper = new MapperFactoryImp().getMapperForEntity(film1);
                System.out.println(mapper.toDTO(film1));
            });

            Actor actor = new ActorDAO().getById(actorDAO.getAll().size() / 2);
            System.out.println(actor);

            //----------------------------------------------------------------
            Actor newActor = new Actor();
            newActor.setName("Trueman");
            actorDAO.create(newActor);

            Film newFilm = new Film();
            newFilm.setTitle("Peanuts");
            filmDAO.create(newFilm);
*/
           // actorDAO.linkFilmWithActor(1, 1);


       // System.out.println(new ActorServiceImpl().getById(4));


       // } catch (SQLException e) {
       //     e.printStackTrace();
        //}


        ActorService serviceA = new ActorServiceImpl();
        Actor actor = new Actor();
        actor.setName("testActor");
        serviceA.create(actor);
        System.out.println(serviceA.getById(actor.getId()));

        FilmService serviceF = new FilmServiceImpl();
        Film film = new Film();
        film.setTitle("testFilm");
        serviceF.create(film);
        System.out.println(serviceF.getById(film.getId()));

        DAO.linkFilmWithActor( film.getId(),actor.getId());
    }
}
