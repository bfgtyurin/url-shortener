package com.vtyurin.app.controller;

import com.vtyurin.app.dao.LinkDao;
import com.vtyurin.app.model.Link;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class HomeFilter extends DelegatingFilterProxy {

    private static final Logger LOGGER = Logger.getLogger(HomeFilter.class);

    @Autowired
    LinkDao linkDao;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String URI = httpRequest.getRequestURI().substring(1);

        if (isValidUrl(URI)) {

            LOGGER.info(URI + " is valid");

            Link existing = linkDao.getByShortUrl(URI);
            LOGGER.info("Got link from db = " + existing);

            if (Objects.nonNull(existing.getId())) {

                LOGGER.info("Redirecting to " + existing.getFullUrl());
                httpResponse.sendRedirect(existing.getFullUrl());

                existing.setClicks(existing.getClicks() + 1);
                linkDao.update(existing);
                return;
            }

        }

        filterChain.doFilter(request, response);
    }

    boolean isValidUrl(String URI) {
        String pattern = "^[a-zA-Z0-9]*$";
        return !(URI.equals("shorten") || URI.length() != 7 || !URI.matches(pattern));
    }
}
