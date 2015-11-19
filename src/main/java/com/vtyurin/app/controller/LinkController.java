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
import java.util.Objects;

public class LinkController implements HttpRequestHandler {
    private final static Logger LOGGER = Logger.getLogger(LinkController.class);

    private String domain;

    @Autowired
    private LinkDao linkDao;

    @Override
    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String METHOD_NAME = "com.vtyurin.app.controller.LinkController.handleRequest";
        LOGGER.info(METHOD_NAME + "HTTP Method : " + req.getMethod() + ", fullLink from request = " + req.getParameter("link"));

        domain = req.getServerName();
        String fullLink = req.getParameter("link");
        handleData(fullLink, resp);
    }

    private void handleData(String fullURL, HttpServletResponse resp) throws IOException {
        if (URL.isValid(fullURL)) {
            Link link = new Link();
            link.setFullURL(fullURL);
            saveLink(link, resp);
        } else {
            sendWarningResponse(resp);
            LOGGER.info("Link from user is invalid");
        }
    }

    void saveLink(Link link, HttpServletResponse resp) throws IOException {
        Link existing = linkDao.getByFullURL(link.getFullURL());
        if (Objects.nonNull(existing.getId())) {
            initByExistingLink(link, existing);
        } else {
            String shortUrl;
            do {
                shortUrl = SequenceGenerator.generate();
            } while (shortUrlInvalid(shortUrl));
            existing = new Link(link.getFullURL(), shortUrl, 0);
            linkDao.save(existing);
        }

        resp.getOutputStream().write((domain + "/" + existing.getShortURL()).getBytes());
    }

    void initByExistingLink(Link link, Link existing) {
        link.setId(existing.getId());
        link.setClicks(existing.getClicks());
        link.setFullURL(existing.getFullURL());
        link.setShortURL(existing.getShortURL());

        LOGGER.info("Returning existing link = " + link);
    }

    boolean shortUrlInvalid(String shortUrl) {
        Link link = linkDao.getByShortUrl(shortUrl);
        return Objects.nonNull(link.getId());
    }

    private void sendWarningResponse(HttpServletResponse resp) throws IOException {
        resp.getOutputStream().write("This is not a valid URL".getBytes());
    }


}
