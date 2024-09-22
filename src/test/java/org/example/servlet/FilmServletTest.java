package org.example.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Actor;
import org.example.model.Film;
import org.example.service.ActorService;
import org.example.service.FilmServiceImpl;
import org.example.servlet.dto.ActorDTO;
import org.example.servlet.dto.FilmDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FilmServletTest {
/*
    private FilmServlet filmServlet;
    private FilmServiceImpl filmService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        filmService = Mockito.mock(FilmServiceImpl.class);
        filmServlet = new FilmServlet(filmService);
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void testDoGet_AllFilms() throws Exception {
        Actor actor1 = new Actor();
        actor1.setName("Actor 1");
        Actor actor2 = new Actor();
        actor2.setName("Actor 2");

        List<Film> films = Arrays.asList(
                new Film(1, "Film One", 2000, List.of(actor1)),
                new Film(2, "Film Two", 2007, List.of(actor2))
        );

        when(filmService.getAll()).thenReturn(films);
        when(request.getPathInfo()).thenReturn("/");

        filmServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        writer.flush();
        String jsonResponse = writer.toString().trim();

        assertEquals(new Gson().toJson(films), jsonResponse);
    }

    @Test
    public void testDoGet_FilmById() throws Exception {
        Actor actor1 = new Actor();
        actor1.setName("Actor 1");
        Film film1 = new Film(1, "Film One", 2000, List.of(actor1));
        when(filmService.getById(1)).thenReturn(film1);
        when(request.getPathInfo()).thenReturn("/1");

        filmServlet.doGet(request, response);

        verify(response).setContentType("application/json");

        writer.flush();
        String jsonResponse = writer.toString().trim();

        String expectedJson = new Gson().toJson(film1);
        assertEquals(expectedJson, jsonResponse);
    }

    @Test
    public void testDoPost_CreateFilm() throws Exception {
        FilmDTO filmDTO = new FilmDTO(0, "New Film", 2000, List.of("New Actor"));
        String json = new Gson().toJson(filmDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(request.getPathInfo()).thenReturn(null);

        filmServlet.doPost(request, response);

        verify(filmService).create(filmDTO);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testDoPut_UpdateFilm() throws Exception {
        FilmDTO filmDTO = new FilmDTO(0, "New Film", 2000, List.of("New Actor"));
        String json = new Gson().toJson(filmDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        filmServlet.doPut(request, response);

        verify(filmService).update(filmDTO);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoDelete_FilmById() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");

        filmServlet.doDelete(request, response);

        verify(filmService).delete(1);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
*/
}
