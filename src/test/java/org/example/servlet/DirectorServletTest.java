package org.example.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Director;
import org.example.service.DirectorServiceImpl;
import org.example.servlet.dto.DirectorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DirectorServletTest {

    private DirectorServlet directorServlet;
    private DirectorServiceImpl directorService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        directorService = Mockito.mock(DirectorServiceImpl.class);
        directorServlet = new DirectorServlet(directorService);
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testDoGet_AllDirectors() throws Exception {
        List<Director> directors = Arrays.asList(
                new Director(1, "Director One", List.of()),
                new Director(2, "Director Two", List.of())
        );
        when(directorService.getAll()).thenReturn(directors);
        when(request.getPathInfo()).thenReturn("/");
        directorServlet.doGet(request, response);
        verify(response).setContentType("application/json");
        writer.flush();
        String jsonResponse = writer.toString().trim();
        assertEquals(new Gson().toJson(directors), jsonResponse);
    }

    @Test
    void testDoGet_DirectorById() throws Exception {
        Director director = new Director(1, "Director One", List.of());
        when(directorService.getById(1)).thenReturn(director);
        when(request.getPathInfo()).thenReturn("/1");
        directorServlet.doGet(request, response);
        verify(response).setContentType("application/json");
        writer.flush();
        String jsonResponse = writer.toString().trim();
        assertEquals(new Gson().toJson(director), jsonResponse);
    }

    @Test
    void testDoGet_DirectorNotFound() throws Exception {
        when(directorService.getById(1)).thenReturn(null);
        when(request.getPathInfo()).thenReturn("/1");
        directorServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testDoGet_SQLExceptionHandling() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");
        doThrow(new SQLException("Database error")).when(directorService).getById(1);
        directorServlet.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
    }

    @Test
    void testDoPost_CreateDirector() throws Exception {
        DirectorDTO directorDTO = new DirectorDTO(0, "New Director", List.of());
        String json = new Gson().toJson(directorDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(request.getPathInfo()).thenReturn(null);
        directorServlet.doPost(request, response);
        verify(directorService).create(directorDTO);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPost_AddFilmToDirector() throws Exception {
        when(request.getPathInfo()).thenReturn("/1/films/2");
        directorServlet.doPost(request, response);
        verify(directorService).addDirectorToFilm(2, 1);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPut_UpdateDirector() throws Exception {
        DirectorDTO directorDTO = new DirectorDTO(1, "Updated Director", List.of());
        String json = new Gson().toJson(directorDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        directorServlet.doPut(request, response);
        verify(directorService).update(directorDTO);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoDelete_DirectorById() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");
        directorServlet.doDelete(request, response);
        verify(directorService).delete(1);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoDelete_SQLExceptionHandling() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");
        doThrow(new SQLException("Database error")).when(directorService).delete(1);
        directorServlet.doDelete(request, response);
        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
    }
}
