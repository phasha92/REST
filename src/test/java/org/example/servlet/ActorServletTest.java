package org.example.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Actor;
import org.example.model.Film;
import org.example.service.ActorService;
import org.example.servlet.dto.ActorDTO;
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
import static org.mockito.Mockito.*;

public class ActorServletTest {

    private ActorServlet actorServlet;
    private ActorService actorService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        actorService = Mockito.mock(ActorService.class);
        actorServlet = new ActorServlet(actorService);
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void testDoGet_AllActors() throws Exception {
        Film film1 = new Film();
        film1.setTitle("Film 1");
        Film film2 = new Film();
        film2.setTitle("Film 2");

        List<Actor> actors = Arrays.asList(
                new Actor(1, "Actor One", List.of(film1)),
                new Actor(2, "Actor Two", List.of(film2))
        );
        when(actorService.getAll()).thenReturn(actors);
        when(request.getPathInfo()).thenReturn("/");

        actorServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        writer.flush();
        String jsonResponse = writer.toString().trim();

        assertEquals(new Gson().toJson(actors), jsonResponse);
    }

    @Test
    public void testDoGet_ActorById() throws Exception {
        Film film1 = new Film();
        film1.setTitle("Film 1");
        Actor actor = new Actor(1, "Actor One", List.of(film1));
        when(actorService.getById(1)).thenReturn(actor);
        when(request.getPathInfo()).thenReturn("/1");

        actorServlet.doGet(request, response);

        verify(response).setContentType("application/json");

        writer.flush();
        String jsonResponse = writer.toString().trim();

        String expectedJson = new Gson().toJson(actor);
        assertEquals(expectedJson, jsonResponse);
    }


    @Test
    public void testDoPost_CreateActor() throws Exception {
        ActorDTO actorDTO = new ActorDTO(0, "New Actor", List.of("New Film"));
        String json = new Gson().toJson(actorDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(request.getPathInfo()).thenReturn(null);

        actorServlet.doPost(request, response);

        verify(actorService).create(actorDTO);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testDoPut_UpdateActor() throws Exception {
        ActorDTO actorDTO = new ActorDTO(1, "Updated Actor", List.of("Updated Film"));
        String json = new Gson().toJson(actorDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        actorServlet.doPut(request, response);

        verify(actorService).update(actorDTO);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoDelete_ActorById() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");

        actorServlet.doDelete(request, response);

        verify(actorService).delete(1);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}
