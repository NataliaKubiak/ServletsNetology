package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var data = service.all();
        sendResponse(response, data);
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var post = service.getById(id);

        sendResponse(response, post.getContent());
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var gson = new Gson();
        final var post = gson.fromJson(body, Post.class);
        final var data = service.save(post);
        sendResponse(response, data);
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        boolean deleted = service.removeById(id);
        if (deleted) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);  // 204 No Content
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);  // 404 Not Found
        }
    }

    private void sendResponse(HttpServletResponse response, Object data) throws IOException {
        final var gson = new Gson();
        response.getWriter().print(gson.toJson(data));
    }
}