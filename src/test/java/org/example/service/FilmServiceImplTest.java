package org.example.service;

import org.example.dao.repository.FilmDAO;
import org.example.dao.repository.LinkActorWithFilm;
import org.example.model.Film;
import org.example.servlet.dto.FilmDTO;
import org.example.servlet.mapper.FilmMapper;
import org.example.servlet.mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceImplTest {
/*
    private FilmDAO mockFilmDAO;
    private Mapper mockMapper;
    private LinkActorWithFilm mockLinkActorWithFilm;
    private FilmServiceImpl filmService;

    @BeforeEach
    void setUp() {
        mockFilmDAO = mock(FilmDAO.class);
        mockMapper = mock(FilmMapper.class);
        mockLinkActorWithFilm = mock(LinkActorWithFilm.class);
        filmService = new FilmServiceImpl(mockFilmDAO, mockMapper, mockLinkActorWithFilm);
    }

    @Test
    void testCreateFilm() throws SQLException {
        FilmDTO filmDTO = new FilmDTO(1, "Matrix",2007, List.of());
        Film film = new Film(1, "Matrix",2007, List.of());

        when(mockMapper.toEntity(filmDTO)).thenReturn(film);

        filmService.create(filmDTO);

        verify(mockFilmDAO).create(film);
    }

    @Test
    void testUpdateFilm() throws SQLException {
        FilmDTO filmDTO = new FilmDTO(1, "Matrix",2007, List.of());
        Film film = new Film(1, "Matrix",2007, List.of());

        when(mockMapper.toEntity(filmDTO)).thenReturn(film);

        filmService.update(filmDTO);

        verify(mockFilmDAO).update(film);
    }

    @Test
    void testDeleteFilm() throws SQLException {
        int filmId = 1;

        filmService.delete(filmId);

        verify(mockFilmDAO).delete(filmId);
    }

    @Test
    void testGetById() throws SQLException {
        Film film = new Film(1, "Matrix",2007, List.of());
        when(filmService.getById(1)).thenReturn(film);

        Film result = filmService.getById(1);

        assertEquals(film, result);
        verify(mockFilmDAO).getById(1);
    }

    @Test
    void testGetAllFilms() throws SQLException {
        Film film1 = new Film(1, "Matrix",2007, List.of());
        Film film2 = new Film(1, "Matrix",2007, List.of());
        when(filmService.getAll()).thenReturn(Arrays.asList(film1, film2));

        List<Film> films = filmService.getAll();

        assertEquals(2, films.size());
        assertEquals(film1, films.get(0));
        assertEquals(film2, films.get(1));
        verify(mockFilmDAO).getAll();
    }


    @Test
    void testAddActorToFilm() throws SQLException {
        int actorId = 1;
        int filmId = 2;

        filmService.addActorToFilm(filmId, actorId);

        verify(mockLinkActorWithFilm).linkFilmWithActor(filmId, actorId);
    }

    @Test
    void testInternalFields() {

        FilmServiceImpl filmService1 = Mockito.spy(new FilmServiceImpl());

        assertNotNull(filmService1.getDao(), "DAO should not be null");
        assertNotNull(filmService1.getMapper(), "Mapper should not be null");

        assertTrue(filmService1.getDao() instanceof FilmDAO, "DAO should be an instance of FilmDAO");
        assertTrue(filmService1.getMapper() instanceof FilmMapper, "Mapper should be an instance of FilmMapper");

        assertNotNull(filmService1.getActorWithFilm(), "LinkActorWithFilm should not be null");
    }
*/
}
