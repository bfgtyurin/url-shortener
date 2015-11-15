package com.vtyurin.app.controller;

import com.vtyurin.app.dao.LinkDao;
import com.vtyurin.app.model.Link;
import com.vtyurin.app.util.RandomSequenceGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LinkController implements HttpRequestHandler {
    private final static Logger LOGGER = Logger.getLogger(LinkController.class);

    @Autowired
    private LinkDao linkDao;

    @Override
    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String METHOD_NAME = "com.vtyurin.app.controller.LinkController.handleRequest";
        LOGGER.info(METHOD_NAME + ": fullLink from request = " + req.getParameter("link"));

        String fullLink = req.getParameter("link");
        String generated = RandomSequenceGenerator.generate();
        Link link = new Link(fullLink, generated, 0);
        linkDao.persist(link);

        resp.getOutputStream().write("shortenLinkFromServlet".getBytes());
    }

}
