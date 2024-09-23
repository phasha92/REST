package org.example.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Film;
import org.example.servlet.dto.FilmDTO;
import org.example.service.FilmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FilmServletTest {

    private FilmServlet filmServlet;
    private FilmService filmService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        filmService = mock(FilmService.class);
        filmServlet = new FilmServlet(filmService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testDoGet_AllFilms() throws Exception {
        Film film1 = new Film(1, "Film 1", 2010, List.of(), null);
        Film film2 = new Film(2, "Film 2", 2012, List.of(), null);
        List<Film> films = Arrays.asList(film1, film2);

        when(filmService.getAll()).thenReturn(films);
        when(request.getPathInfo()).thenReturn("/");

        filmServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        writer.flush();
        String jsonResponse = writer.toString().trim();
        assertEquals(new Gson().toJson(films), jsonResponse);
    }

    @Test
    void testDoGet_FilmById() throws Exception {
        Film film = new Film(1, "Film 1", 2010, List.of(), null);
        when(filmService.getById(1)).thenReturn(film);
        when(request.getPathInfo()).thenReturn("/1");

        filmServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        writer.flush();
        String jsonResponse = writer.toString().trim();
        String expectedJson = new Gson().toJson(film);
        assertEquals(expectedJson, jsonResponse);
    }

    @Test
    void testDoGet_FilmNotFound() throws Exception {
        when(request.getPathInfo()).thenReturn("/999");
        when(filmService.getById(999)).thenReturn(null);

        filmServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testDoPost_CreateFilm() throws Exception {
        FilmDTO filmDTO = new FilmDTO(0, "New Film", 2023, List.of("Actor 1"), "Director 1");
        String json = new Gson().toJson(filmDTO);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(request.getPathInfo()).thenReturn(null);

        filmServlet.doPost(request, response);

        verify(filmService).create(filmDTO);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPost_AddActorToFilm() throws Exception {
        when(request.getPathInfo()).thenReturn("/1/actors/2");

        filmServlet.doPost(request, response);

        verify(filmService).addActorToFilm(1, 2);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPost_AddDirectorToFilm() throws Exception {
        when(request.getPathInfo()).thenReturn("/1/directors/3");

        filmServlet.doPost(request, response);

        verify(filmService).addDirectorToFilm(1, 3);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPut_UpdateFilm() throws Exception {
        FilmDTO filmDTO = new FilmDTO(1, "Updated Film", 2020, List.of("Updated Actor"), "Updated Director");
        String json = new Gson().toJson(filmDTO);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        filmServlet.doPut(request, response);

        verify(filmService).update(filmDTO);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoDelete_FilmById() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");

        filmServlet.doDelete(request, response);

        verify(filmService).delete(1);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testSQLExceptionHandling() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");
        doThrow(new SQLException("Database error")).when(filmService).getById(1);

        filmServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
    }
}
