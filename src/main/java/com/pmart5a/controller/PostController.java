package com.pmart5a.controller;

import com.google.gson.Gson;
import com.pmart5a.exception.NotFoundException;
import com.pmart5a.model.Post;
import com.pmart5a.service.PostService;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@Controller
public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    public static final String POST_NOT_FOUND = "Post not found.";
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var data = service.all();
        final var gson = new Gson();
        response.getWriter().print(gson.toJson(data));
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        try {
            response.setContentType(APPLICATION_JSON);
            final var data = service.getById(id);
            final var gson = new Gson();
            response.getWriter().print(gson.toJson(data));
        } catch (NotFoundException e) {
            sendPostNotFound(response);
        }
    }

    public void save(Reader body, HttpServletResponse response) throws Exception {
        try {
            response.setContentType(APPLICATION_JSON);
            final var gson = new Gson();
            final var post = gson.fromJson(body, Post.class);
            final var data = service.save(post);
            response.getWriter().print(gson.toJson(data));
        } catch (NotFoundException e) {
            sendPostNotFound(response);
        }
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        try {
            service.removeById(id);
        } catch (NotFoundException e) {
            sendPostNotFound(response);
        }
    }

    private void sendPostNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, POST_NOT_FOUND);
    }
}