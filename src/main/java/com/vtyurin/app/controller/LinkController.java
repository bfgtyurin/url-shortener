package com.vtyurin.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtyurin.app.dao.LinkDao;
import com.vtyurin.app.model.Link;
import com.vtyurin.app.util.SequenceGenerator;
import com.vtyurin.app.util.URL;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.vtyurin.app.util.SequenceGenerator.SEQUENCE_LENGTH;

public class LinkController implements HttpRequestHandler {
    private final static Logger LOGGER = Logger.getLogger(LinkController.class);

    private static final String SHORT_URL_COOKIE_NAME = "shortUrls";
    private static final String SHORT_URL_DELIMITER = "/";

    @Autowired
    private LinkDao linkDao;

    @Override
    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String METHOD_NAME = "com.vtyurin.app.controller.LinkController.handleRequest";
        final String REQUEST_METHOD = req.getMethod();

        if ("POST".equals(REQUEST_METHOD)) {
            String fullUrl = req.getParameter("fullUrl");
            LOGGER.info(METHOD_NAME + " HTTP Method : " + REQUEST_METHOD + ", fullUrl parameter value = " + fullUrl);

            handlePostRequest(fullUrl, resp);
        } else if ("GET".equals(REQUEST_METHOD)) {
            Cookie[] cookies = req.getCookies();
            for (Cookie cookie : cookies) {
                if (SHORT_URL_COOKIE_NAME.equals(cookie.getName())) {
                    String cookieValue = cookie.getValue();
                    LOGGER.info(METHOD_NAME + " HTTP Method : " + REQUEST_METHOD + ", cookieValue = " + cookieValue);
                    handleGetRequest(cookieValue, resp);
                }
            }
        }
    }

    void handlePostRequest(String fullUrl, HttpServletResponse resp) {
        if (URL.isValid(fullUrl)) {
            Link link = getExistingLinkObjectOrCreateNew(fullUrl);
            sendResponse(link, resp);
        } else {
            sendWarningResponse(resp);
            LOGGER.info("Link from user is invalid");
        }
    }

    void handleGetRequest(String shortUrls, HttpServletResponse resp) throws IOException {
        List<Link> links = tryCreateLinkListWithShortUrls(shortUrls);
        sendResponse(links, resp);
    }

    Link getExistingLinkObjectOrCreateNew(String fullUrl) {
        Link link = linkDao.getByFullUrl(fullUrl);
        if (Objects.isNull(link.getId())) {
            link = createNewLinkObjectWithUrl(fullUrl);
        }

        return link;
    }

    Link createNewLinkObjectWithUrl(String fullUrl) {
        final String METHOD_NAME = "com.vtyurin.app.controller.LinkController.createNewLinkObjectWithUrl ";

        String shortUrl;
        do {
            shortUrl = SequenceGenerator.generate();
        } while (shortUrlInvalid(shortUrl));
        String title = getPageTitle(fullUrl);
        Link link = new Link(fullUrl, shortUrl, 0);
        link.setTitle(title);
        linkDao.save(link);

        LOGGER.info(METHOD_NAME + "return = " + link);
        return link;
    }

    String getPageTitle(String fullUrl) {
        final String METHOD_NAME = "com.vtyurin.app.controller.LinkController.getPageTitle ";

        Document doc = null;
        try {
            doc = Jsoup.connect(fullUrl).get();
        } catch (IOException e) {
            LOGGER.error(e);
        }

        String title;
        if (doc != null) {
            title = doc.title();
        } else {
            title = "Title not available";
        }

        LOGGER.info(METHOD_NAME + " return = " + title);
        return title;
    }

    List<Link> tryCreateLinkListWithShortUrls(String shortUrls) {
        List<Link> links;
        try {
            links = createLinkListWithShortUrls(shortUrls);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e);
            links = Collections.emptyList();
        }

        return Collections.unmodifiableList(links);
    }

    List<Link> createLinkListWithShortUrls(String shortUrls) {
        if (shortUrls.length() < 1) {
            throw new IllegalArgumentException("illegal shortUrl string length [" + shortUrls.length() + "]");
        }
        if (!shortUrls.contains(SHORT_URL_DELIMITER)) {
            throw new IllegalArgumentException("illegal shortUrl string format, it should be like [string1:string2]");
        }

        String[] array = shortUrls.split(SHORT_URL_DELIMITER);
        List<Link> links = new ArrayList<>();
        for (String s : array) {
            if (s.length() != SEQUENCE_LENGTH) {
                throw new IllegalArgumentException("illegal shortUrl string length [" + s.length() + "]");
            }
            links.add(linkDao.getByShortUrl(s));
        }

        return Collections.unmodifiableList(links);
    }

    void sendResponse(Object object, HttpServletResponse resp) {
        final String METHOD_NAME = "com.vtyurin.app.controller.LinkController.sendResponse ";

        try {
            sendJsonResponse(object, resp);
        } catch (IOException e) {
            LOGGER.error(METHOD_NAME + e);
        }
    }

    void sendJsonResponse(Object object, HttpServletResponse resp) throws IOException {
        final String METHOD_NAME = "com.vtyurin.app.controller.LinkController.sendJsonResponse ";

        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), object);
        LOGGER.info(METHOD_NAME + " sending json response to user = " + object);
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
