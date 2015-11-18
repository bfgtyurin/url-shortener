package com.vtyurin.app.controller;

import com.vtyurin.app.dao.LinkDao;
import com.vtyurin.app.model.Link;
import com.vtyurin.app.util.SequenceGenerator;
import com.vtyurin.app.util.URL;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LinkController implements HttpRequestHandler {
    private final static Logger LOGGER = Logger.getLogger(LinkController.class);

    private String domain;

    @Autowired
    private LinkDao linkDao;

    @Override
    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String METHOD_NAME = "com.vtyurin.app.controller.LinkController.handleRequest";
        LOGGER.info(METHOD_NAME + ": fullLink from request = " + req.getParameter("link"));

        domain = req.getServerName();
        String fullLink = req.getParameter("link");
        handleData(fullLink, resp);
    }

    private void handleData(String fullURL, HttpServletResponse resp) throws IOException {
        if (URL.isValid(fullURL)) {
            sendNormalResponse(fullURL, resp);
        } else {
            sendWarningResponse(resp);
            LOGGER.info("Link from user is invalid");
        }
    }

    private void sendWarningResponse(HttpServletResponse resp) throws IOException {
        resp.getOutputStream().write("This is not a valid URL".getBytes());
    }

    private void sendNormalResponse(String fullURL, HttpServletResponse resp) throws IOException {
        Link link = new Link(fullURL, "", 0);
        linkDao.persistWithTransaction(link);
        resp.getOutputStream().write((domain + "/" + link.getShortURL()).getBytes());
    }

}
