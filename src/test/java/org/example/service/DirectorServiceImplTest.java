package org.example.service;

import org.example.dao.repository.DirectorDAO;
import org.example.dao.repository.util.link.LinkDirectorWithFilm;
import org.example.model.Director;
import org.example.servlet.dto.DirectorDTO;
import org.example.servlet.mapper.DirectorMapper;
import org.example.servlet.mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectorServiceImplTest {

    private DirectorDAO mockDirectorDAO;
    private Mapper mockMapper;
    private LinkDirectorWithFilm mockLinkDirectorWithFilm;
    private DirectorServiceImpl directorService;

    @BeforeEach
    void setUp() {
        mockDirectorDAO = mock(DirectorDAO.class);
        mockMapper = mock(Mapper.class);
        mockLinkDirectorWithFilm = mock(LinkDirectorWithFilm.class);
        directorService = new DirectorServiceImpl(mockDirectorDAO, mockMapper, mockLinkDirectorWithFilm);
    }

    @Test
    void testCreateDirector() throws SQLException {
        DirectorDTO directorDTO = new DirectorDTO(1, "John Smith", List.of());
        Director director = new Director(1, "John Smith", List.of());
        when(mockMapper.toEntity(directorDTO)).thenReturn(director);
        directorService.create(directorDTO);
        verify(mockDirectorDAO).create(director);
    }

    @Test
    void testUpdateDirector() throws SQLException {
        DirectorDTO directorDTO = new DirectorDTO(1, "Jane Smith", List.of());
        Director director = new Director(1, "Jane Smith", List.of());
        when(mockMapper.toEntity(directorDTO)).thenReturn(director);
        directorService.update(directorDTO);
        verify(mockDirectorDAO).update(director);
    }

    @Test
    void testDeleteDirector() throws SQLException {
        int directorId = 1;
        directorService.delete(directorId);
        verify(mockDirectorDAO).delete(directorId);
    }

    @Test
    void testGetById() throws SQLException {
        Director director = new Director(1, "John Smith", List.of());
        when(mockDirectorDAO.getById(1)).thenReturn(director);
        Director result = directorService.getById(1);
        assertEquals(director, result);
        verify(mockDirectorDAO).getById(1);
    }

    @Test
    void testGetAllDirectors() throws SQLException {
        Director director1 = new Director(1, "John Smith", List.of());
        Director director2 = new Director(2, "Jane Smith", List.of());
        when(mockDirectorDAO.getAll()).thenReturn(Arrays.asList(director1, director2));
        List<Director> directors = directorService.getAll();
        assertEquals(2, directors.size());
        assertEquals(director1, directors.get(0));
        assertEquals(director2, directors.get(1));
        verify(mockDirectorDAO).getAll();
    }

    @Test
    void testAddDirectorToFilm() throws SQLException {
        int directorId = 1;
        int filmId = 2;
        directorService.addDirectorToFilm(filmId, directorId);
        verify(mockLinkDirectorWithFilm).linkFilmWithDirector(filmId, directorId);
    }

    @Test
    void testInternalFields() {
        DirectorServiceImpl directorService2 = Mockito.spy(new DirectorServiceImpl());
        assertNotNull(directorService2.getDirectorDAO(), "DAO should not be null");
        assertNotNull(directorService2.getMapper(), "Mapper should not be null");
        assertTrue(directorService2.getDirectorDAO() instanceof DirectorDAO, "DAO should be an instance of DirectorDAO");
        assertTrue(directorService2.getMapper() instanceof DirectorMapper, "Mapper should be an instance of DirectorMapper");
        assertNotNull(directorService2.getDirectorWithFilm(), "LinkDirectorWithFilm should not be null");
    }
}
