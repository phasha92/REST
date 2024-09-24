package org.example.service;

import org.example.dao.repository.FilmDAO;
import org.example.dao.repository.util.EntityExistenceChecker;
import org.example.dao.repository.util.link.LinkActorWithFilm;
import org.example.dao.repository.util.link.LinkDirectorWithFilm;
import org.example.model.Director;
import org.example.model.Film;
import org.example.servlet.dto.FilmDTO;
import org.example.servlet.mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilmServiceImplTest {

    private FilmDAO mockFilmDAO;
    private Mapper mockMapper;
    private LinkActorWithFilm mockLinkActorWithFilm;
    private LinkDirectorWithFilm mockLinkDirectorWithFilm;
    private FilmServiceImpl filmService;
    private EntityExistenceChecker mockEntityExistenceChecker;

    @BeforeEach
    void setUp() {
        mockFilmDAO = mock(FilmDAO.class);
        mockMapper = mock(Mapper.class);
        mockLinkActorWithFilm = mock(LinkActorWithFilm.class);
        mockLinkDirectorWithFilm = mock(LinkDirectorWithFilm.class);
        mockEntityExistenceChecker = mock(EntityExistenceChecker.class);
        filmService = new FilmServiceImpl(mockFilmDAO, mockMapper, mockLinkActorWithFilm, mockLinkDirectorWithFilm);
    }

    @Test
    void testCreateFilm() throws SQLException {
        FilmDTO filmDTO = new FilmDTO(1, "Inception", 2010, List.of("Leonardo DiCaprio"), "Christopher Nolan");
        Film film = new Film(1, "Inception", 2010, List.of(), new Director(1, "Christopher Nolan", List.of()));
        when(mockMapper.toEntity(filmDTO)).thenReturn(film);
        filmService.create(filmDTO);
        verify(mockFilmDAO).create(film);
    }

    @Test
    void testUpdateFilm() throws SQLException {
        FilmDTO filmDTO = new FilmDTO(1, "Interstellar", 2014, List.of("Matthew McConaughey"), "Christopher Nolan");
        Film film = new Film(1, "Interstellar", 2014, List.of(), new Director(1, "Christopher Nolan", List.of()));
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
        Film film = new Film(1, "Inception", 2010, List.of(), new Director(1, "Christopher Nolan", List.of()));
        when(mockFilmDAO.getById(1)).thenReturn(film);
        Film result = filmService.getById(1);
        assertEquals(film, result);
        verify(mockFilmDAO).getById(1);
    }

    @Test
    void testGetAllFilms() throws SQLException {
        Film film1 = new Film(1, "Inception", 2010, List.of(), new Director(1, "Christopher Nolan", List.of()));
        Film film2 = new Film(2, "Interstellar", 2014, List.of(), new Director(1, "Christopher Nolan", List.of()));
        when(mockFilmDAO.getAll()).thenReturn(Arrays.asList(film1, film2));
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
    void testAddDirectorToFilm() throws SQLException {
        int directorId = 1;
        int filmId = 2;
        filmService.addDirectorToFilm(filmId, directorId);
        verify(mockLinkDirectorWithFilm).linkFilmWithDirector(filmId, directorId);
    }

    @Test
    void testInternalFields() {
        FilmServiceImpl filmService2 = Mockito.spy(filmService);
        assertNotNull(filmService2.getDao(), "DAO should not be null");
        assertNotNull(filmService2.getMapper(), "Mapper should not be null");
        assertNotNull(filmService2.getActorWithFilm(), "LinkActorWithFilm should not be null");
        assertNotNull(filmService2.getDirectorWithFilm(), "LinkDirectorWithFilm should not be null");
    }
}
