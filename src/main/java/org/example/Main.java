package org.example;

import org.example.dao.repository.util.EntityExistenceChecker;
import org.example.dao.repository.util.link.LinkActorWithFilm;
import org.example.dao.repository.util.link.LinkDirectorWithFilm;

public class Main {
    public static void main(String[] args) throws Exception{
        LinkActorWithFilm linkActorWithFilm = new LinkActorWithFilm();

       // linkActorWithFilm.linkFilmWithActor(113,8);
        LinkDirectorWithFilm linkDirectorWithFilm = new LinkDirectorWithFilm();
        linkDirectorWithFilm.linkFilmWithDirector(113,1);

    }
}
