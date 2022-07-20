package com.pmart5a.servlet;

import com.pmart5a.controller.PostController;
import com.pmart5a.exception.NotFoundException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_DELETE = "DELETE";
    public static final String PATH_POSTS = "/api/posts";
    public static final String PATH_ROOT = "/";
    public static final String REGEX_POSTS = "/api/posts/\\d+";
    public static final String POST_NOT_FOUND = "Post not found.";
    private PostController controller;

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext("com.pmart5a");
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
        } catch (NotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, POST_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Long parseId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf(PATH_ROOT) + 1));
    }
}