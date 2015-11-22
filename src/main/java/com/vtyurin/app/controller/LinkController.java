package com.vtyurin.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Objects;

public class LinkController implements HttpRequestHandler {
    private final static Logger LOGGER = Logger.getLogger(LinkController.class);

    @Autowired
    private LinkDao linkDao;

    @Override
    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String METHOD_NAME = "com.vtyurin.app.controller.LinkController.handleRequest";
        final String REQUEST_METHOD = req.getMethod();
        LOGGER.info(METHOD_NAME + "HTTP Method : " + REQUEST_METHOD + ", fullLink from request = " + req.getParameter("link"));

        if ("POST".equals(REQUEST_METHOD)) {
            String fullLink = req.getParameter("link");
            handlePostRequest(fullLink, resp);
        }
    }

    void handlePostRequest(String fullURL, HttpServletResponse resp) {
        if (URL.isValid(fullURL)) {
            Link link = getExistingLinkObjectOrCreateNew(fullURL);
            sendLinkResponse(link, resp);
        } else {
            sendWarningResponse(resp);
            LOGGER.info("Link from user is invalid");
        }
    }

    Link getExistingLinkObjectOrCreateNew(String fullURL) {
        Link link = linkDao.getByFullURL(fullURL);
        if (Objects.isNull(link.getId())) {
            link = createNewLinkObjectWithUrl(fullURL);
        }

        return link;
    }

    Link createNewLinkObjectWithUrl(String fullURL) {
        String shortUrl;
        do {
            shortUrl = SequenceGenerator.generate();
        } while (shortUrlInvalid(shortUrl));
        Link link = new Link(fullURL, shortUrl, 0);
        linkDao.save(link);
        LOGGER.info("New Link created = " + link);

        return link;
    }

    void sendLinkResponse(Link link, HttpServletResponse resp) {
        try {
            sendLinkJsonResponse(link, resp);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    void sendLinkJsonResponse(Link link, HttpServletResponse resp) throws IOException {
        LOGGER.info("Returning link to user = " + link);
        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), link);
    }

    boolean shortUrlInvalid(String shortUrl) {
        Link link = linkDao.getByShortUrl(shortUrl);
        return Objects.nonNull(link.getId());
    }

    private void sendWarningResponse(HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            resp.sendError(400, "This is not a valid URL");
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
