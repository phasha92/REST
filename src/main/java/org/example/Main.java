package org.example;

import org.example.dao.DBConnectManager;
import org.example.dao.repository.ActorDAO;
import org.example.model.Entity;
import org.example.servlet.ActorServlet;
import org.example.servlet.dto.DTO;
import org.example.model.Actor;
import org.example.model.Film;

import org.example.servlet.mapper.Mapper;
import org.example.servlet.mapper.factory.MapperFactory;
import org.example.servlet.mapper.factory.MapperFactoryImp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
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
        ActorDAO actorDAO = new ActorDAO();

        try {
            // 1. Создание нового актера
           Actor actor = new Actor();
            actor.setName("Robert Downey Stark.");
            actorDAO.createActor(actor);
            System.out.println("Actor created: " + actor);

            // 2. Получение актера по ID
            Actor retrievedActor = actorDAO.getActorById(actor.getId());
            System.out.println("Actor retrieved: " + retrievedActor);

            // 3. Обновление актера
            actor.setName("Updated Name");
            actorDAO.updateActor(actor);
            System.out.println("Actor updated: " + actorDAO.getActorById(actor.getId()));

            // 4. Удаление актера
             actorDAO.deleteActor(actor.getId());
            System.out.println("Actor deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
