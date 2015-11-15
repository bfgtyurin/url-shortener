package com.vtyurin.app.controller;

import com.vtyurin.app.dao.LinkDao;
import com.vtyurin.app.model.Link;
import com.vtyurin.app.util.ConnectionUtil;
import com.vtyurin.app.util.RandomSequenceGenerator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ajaxtest")
public class AjaxTestController extends HttpServlet {
    private final static Logger LOGGER = Logger.getLogger(AjaxTestController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String METHOD_NAME = "com.vtyurin.app.controller.AjaxTestController.doGet";
        LOGGER.info(METHOD_NAME + ": fullLink from req = " + req.getParameter("link"));

        final LinkDao linkDao = new LinkDao(ConnectionUtil.getProdConnection());
        String fullLink = req.getParameter("link");
        String generated = RandomSequenceGenerator.generate();
        Link link = new Link(fullLink, generated, 0);
        linkDao.persist(link);

        resp.getOutputStream().write("shortenLinkFromServlet".getBytes());
    }
}
