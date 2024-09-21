package org.example.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Actor;
import org.example.service.ActorService;
import org.example.service.ActorServiceImpl;
import org.example.servlet.dto.ActorDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/actors/*")
public class ActorServlet extends HttpServlet {

    private transient ActorService actorService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        actorService = new ActorServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Actor> actors = actorService.getAll();
                response.setContentType("application/json");
                response.getWriter().write(new Gson().toJson(actors));
            } else {
                int actorId = Integer.parseInt(pathInfo.substring(1)); // Получаем ID из URL
                Actor actor = actorService.getById(actorId);
                if (actor != null) {
                    response.setContentType("application/json");
                    response.getWriter().write(new Gson().toJson(actor));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo != null && pathInfo.matches("/\\d+/films/\\d+")) {
                // Извлекаем ID актёра и фильма из URL
                String[] pathParts = pathInfo.split("/");
                int actorId = Integer.parseInt(pathParts[1]);
                int filmId = Integer.parseInt(pathParts[3]);

                // Добавляем фильм к актёру
                actorService.addFilmToActor(actorId, filmId);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                // Обычный POST-запрос для создания нового актёра
                ActorDTO actorDTO = new Gson().fromJson(request.getReader(), ActorDTO.class);
                actorService.create(actorDTO);
                response.setStatus(HttpServletResponse.SC_CREATED);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ActorDTO actorDTO = new Gson().fromJson(request.getReader(), ActorDTO.class);
            actorService.update(actorDTO);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try {
            int actorId = Integer.parseInt(pathInfo.substring(1)); // Получаем ID из URL
            actorService.delete(actorId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
