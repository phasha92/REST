package org.example.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Director;
import org.example.service.DirectorService;
import org.example.service.DirectorServiceImpl;
import org.example.servlet.dto.DirectorDTO;
import org.example.servlet.mapper.DirectorMapper;
import org.example.servlet.mapper.Mapper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/directors/*")
public class DirectorServlet extends HttpServlet {

    private transient DirectorServiceImpl directorService;

    public DirectorServlet(DirectorServiceImpl directorService) {
        this.directorService = directorService;
    }

    public DirectorServlet() {
    }

    @Override
    public void init() throws ServletException {
        super.init();
        directorService = new DirectorServiceImpl(); // Инициализация сервиса режиссеров
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Director> directors = directorService.getAll();
                response.setContentType("application/json");
                response.getWriter().write(new Gson().toJson(directors));
            }else {
                int directorId = Integer.parseInt(pathInfo.substring(1));
                Director director = directorService.getById(directorId);
                if (director != null) {
                    response.setContentType("application/json");
                    response.getWriter().write(new Gson().toJson(director));
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
                // Извлекаем ID режиссера и фильма из URL
                String[] pathParts = pathInfo.split("/");
                int directorId = Integer.parseInt(pathParts[1]);
                int filmId = Integer.parseInt(pathParts[3]);

                // Добавляем фильм к режиссеру
                directorService.addDirectorToFilm(filmId, directorId);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                // Обычный POST-запрос для создания нового режиссера
                DirectorDTO directorDTO = new Gson().fromJson(request.getReader(), DirectorDTO.class);
                directorService.create(directorDTO);
                response.setStatus(HttpServletResponse.SC_CREATED);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            DirectorDTO directorDTO = new Gson().fromJson(request.getReader(), DirectorDTO.class);
            directorService.update(directorDTO);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try {
            int directorId = Integer.parseInt(pathInfo.substring(1)); // Получаем ID из URL
            directorService.delete(directorId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
