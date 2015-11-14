package com.vtyurin.app.controller;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class HomeController extends HttpServlet {

    private final static Logger LOGGER = Logger.getLogger(AjaxTestController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info(req.getRequestURL());
        LOGGER.info(req.getRequestURI());
    }
}
