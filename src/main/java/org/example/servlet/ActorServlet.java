package org.example.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Actor;
import org.example.service.ActorServiceImpl;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/actor/*")
public class ActorServlet extends HttpServlet {

    private ActorServiceImpl actorService;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        try {
            resp.getWriter().println("<h1>Actor Servlet</h1>");
            resp.getWriter().println("<h2><a href=\"http://localhost:8080/\">Вернуться</a></h2>");

            for (Actor actor : actorService.getAll()) {
                resp.getWriter().println("<h3>" + actor + "</h3>");
            }
        } catch (SQLException e) {
            // Логирование и отправка ошибки клиенту
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error accessing database");
            e.printStackTrace();
        } catch (IOException e) {
            // Логирование и отправка ошибки клиенту
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error writing response");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
