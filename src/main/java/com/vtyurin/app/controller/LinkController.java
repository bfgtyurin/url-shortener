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

        if ("POST".equals(REQUEST_METHOD)) {
            String fullUrl = req.getParameter("fullUrl");
            handlePostRequest(fullUrl, resp);

            LOGGER.info(METHOD_NAME + "HTTP Method : " + REQUEST_METHOD + ", fullUrl from request = " + fullUrl);
        } else if ("GET".equals(REQUEST_METHOD)) {
            String shortUrls = req.getParameter("shortUrls");
            resp.getOutputStream().write("stub".getBytes());
            LOGGER.info(METHOD_NAME + "HTTP Method : " + REQUEST_METHOD + ", shortLinks from request = " + shortUrls);
        }
    }

    void handlePostRequest(String fullUrl, HttpServletResponse resp) {
        if (URL.isValid(fullUrl)) {
            Link link = getExistingLinkObjectOrCreateNew(fullUrl);
            sendLinkResponse(link, resp);
        } else {
            sendWarningResponse(resp);
            LOGGER.info("Link from user is invalid");
        }
    }

    Link getExistingLinkObjectOrCreateNew(String fullUrl) {
        Link link = linkDao.getByFullURL(fullUrl);
        if (Objects.isNull(link.getId())) {
            link = createNewLinkObjectWithUrl(fullUrl);
        }

        return link;
    }

    Link createNewLinkObjectWithUrl(String fullUrl) {
        String shortUrl;
        do {
            shortUrl = SequenceGenerator.generate();
        } while (shortUrlInvalid(shortUrl));
        Link link = new Link(fullUrl, shortUrl, 0);
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
