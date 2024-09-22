package org.example.service;

import org.example.dao.repository.ActorDAO;
import org.example.dao.repository.LinkActorWithFilm;
import org.example.model.Actor;
import org.example.servlet.dto.ActorDTO;
import org.example.servlet.mapper.ActorMapper;
import org.example.servlet.mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActorServiceImplTest {

    private ActorDAO mockActorDAO;
    private Mapper mockMapper;
    private LinkActorWithFilm mockLinkActorWithFilm;
    private ActorServiceImpl actorService;

    @BeforeEach
    void setUp() {
        mockActorDAO = mock(ActorDAO.class);
        mockMapper = mock(Mapper.class);
        mockLinkActorWithFilm = mock(LinkActorWithFilm.class);
        actorService = new ActorServiceImpl(mockActorDAO, mockMapper, mockLinkActorWithFilm);
    }

    @Test
    void testCreateActor() throws SQLException {
        ActorDTO actorDTO = new ActorDTO(1, "John Doe", List.of());
        Actor actor = new Actor(1, "John Doe", List.of());

        when(mockMapper.toEntity(actorDTO)).thenReturn(actor);

        actorService.create(actorDTO);

        verify(mockActorDAO).create(actor);
    }

    @Test
    void testUpdateActor() throws SQLException {
        ActorDTO actorDTO = new ActorDTO(1, "Jane Doe", List.of());
        Actor actor = new Actor(1, "Jane Doe", List.of());

        when(mockMapper.toEntity(actorDTO)).thenReturn(actor);

        actorService.update(actorDTO);

        verify(mockActorDAO).update(actor);
    }

    @Test
    void testDeleteActor() throws SQLException {
        int actorId = 1;

        actorService.delete(actorId);

        verify(mockActorDAO).delete(actorId);
    }

    @Test
    void testGetById() throws SQLException {
        Actor actor = new Actor(1, "John Doe", List.of());
        when(mockActorDAO.getById(1)).thenReturn(actor);

        Actor result = actorService.getById(1);

        assertEquals(actor, result);
        verify(mockActorDAO).getById(1);
    }

    @Test
    void testGetAllActors() throws SQLException {
        Actor actor1 = new Actor(1, "John Doe", List.of());
        Actor actor2 = new Actor(2, "Jane Doe", List.of());
        when(mockActorDAO.getAll()).thenReturn(Arrays.asList(actor1, actor2));

        List<Actor> actors = actorService.getAll();

        assertEquals(2, actors.size());
        assertEquals(actor1, actors.get(0));
        assertEquals(actor2, actors.get(1));
        verify(mockActorDAO).getAll();
    }


    @Test
    void testAddFilmToActor() throws SQLException {
        int actorId = 1;
        int filmId = 2;

        actorService.addFilmToActor(actorId, filmId);

        verify(mockLinkActorWithFilm).linkFilmWithActor(filmId, actorId);
    }

    @Test
    void testInternalFields() {
        // Создаем экземпляр ActorServiceImpl с пустым конструктором
        ActorServiceImpl actorService2 = Mockito.spy(new ActorServiceImpl());

        // Проверяем, что dao и mapper не равны null
        assertNotNull(actorService2.getDao(), "DAO should not be null");
        assertNotNull(actorService2.getMapper(), "Mapper should not be null");

        // Проверяем, что dao и mapper являются экземплярами ожидаемых классов
        assertTrue(actorService2.getDao() instanceof ActorDAO, "DAO should be an instance of ActorDAO");
        assertTrue(actorService2.getMapper() instanceof ActorMapper, "Mapper should be an instance of ActorMapper");

        // Если ты добавил поле actorWithFilm, проверь его тоже
        assertNotNull(actorService2.getActorWithFilm(), "LinkActorWithFilm should not be null");
    }
}
