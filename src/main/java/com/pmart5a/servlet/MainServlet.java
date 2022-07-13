package com.pmart5a.servlet;

import com.pmart5a.controller.PostController;
import com.pmart5a.repository.PostRepository;
import com.pmart5a.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_DELETE = "DELETE";
    public static final String PATH_POSTS = "/api/posts";
    public static final String PATH_ROOT = "/";
    public static final String REGEX_POSTS = "/api/posts/\\d+";
    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            if (method.equals(METHOD_GET) && path.equals(PATH_POSTS)) {
                controller.all(resp);
                return;
            }
            if (method.equals(METHOD_GET) && path.matches(REGEX_POSTS)) {
                final var id = parseId(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals(METHOD_POST) && path.equals(PATH_POSTS)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(METHOD_DELETE) && path.matches(REGEX_POSTS)) {
                // easy way
                final var id = parseId(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Long parseId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf(PATH_ROOT) + 1));
    }
}