package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;

    //вынесла константы
    private static final String API_POSTS_PATH = "/api/posts";
    private static final String API_POSTS_ID_PATTERN = "/api/posts/\\d+";
    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_DELETE = "DELETE";

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            //path и method вынесла в переменные
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            if (HTTP_GET.equals(method) && API_POSTS_PATH.equals(path)) {
                controller.all(resp);
                return;
            }
            if (HTTP_GET.equals(method) && path.matches(API_POSTS_ID_PATTERN)) {
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                controller.getById(id, resp);
                return;
            }
            if (HTTP_POST.equals(method) && API_POSTS_PATH.equals(path)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (HTTP_DELETE.equals(method) && path.matches(API_POSTS_ID_PATTERN)) {
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

