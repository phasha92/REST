package org.example.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Film;
import org.example.service.FilmService;
import org.example.service.FilmServiceImpl;
import org.example.servlet.dto.FilmDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/films/*")
public class FilmServlet extends HttpServlet {

    private transient FilmService filmService;

    public FilmServlet(FilmService filmService) {
        this.filmService = filmService;
    }

    public FilmServlet() {
    }

    @Override
    public void init() throws ServletException {
        super.init();
        filmService = new FilmServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Film> films = filmService.getAll();
                response.setContentType("application/json");
                response.getWriter().write(new Gson().toJson(films));
            } else {
                int filmId = Integer.parseInt(pathInfo.substring(1)); // Получаем ID из URL
                Film film = filmService.getById(filmId);
                if (film != null) {
                    response.setContentType("application/json");
                    response.getWriter().write(new Gson().toJson(film));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo != null && pathInfo.matches("/\\d+/actors/\\d+")) {
                // Извлекаем ID фильма и актёра из URL
                String[] pathParts = pathInfo.split("/");
                int filmId = Integer.parseInt(pathParts[1]);
                int actorId = Integer.parseInt(pathParts[3]);

                // Добавляем актёра к фильму
                filmService.addActorToFilm(filmId, actorId);
                response.setStatus(HttpServletResponse.SC_OK);
            } else if (pathInfo != null && pathInfo.matches("/\\d+/directors/\\d+")) {
                // Извлекаем ID фильма и режиссёра из URL
                String[] pathParts = pathInfo.split("/");
                int filmId = Integer.parseInt(pathParts[1]);
                int directorId = Integer.parseInt(pathParts[3]);

                // Добавляем режиссёра к фильму
                filmService.addDirectorToFilm(filmId, directorId);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                // Обычный POST-запрос для создания нового фильма
                FilmDTO filmDTO = new Gson().fromJson(request.getReader(), FilmDTO.class);
                filmService.create(filmDTO);
                response.setStatus(HttpServletResponse.SC_CREATED);
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            FilmDTO filmDTO = new Gson().fromJson(request.getReader(), FilmDTO.class);
            filmService.update(filmDTO);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        try {
            int filmId = Integer.parseInt(pathInfo.substring(1)); // Получаем ID из URL
            filmService.delete(filmId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
