package com.vtyurin.app.controller;

import com.vtyurin.app.db.DBHelper;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/ajaxtest")
public class AjaxTestController extends HttpServlet {
    private final static Logger LOGGER = Logger.getLogger(AjaxTestController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String METHOD_NAME = "com.vtyurin.app.controller.AjaxTestController.doGet";
        LOGGER.info(METHOD_NAME + ": fullLink from req = " + req.getParameter("link"));

        String fullLink = req.getParameter("link");
        String generated = "dummy";
        try (Connection connection = DBHelper.getInstance().getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("INSERT INTO Links (fullLink, shortLink) VALUES (?, ?)")) {
                int idx = 1;
                pst.setString(idx++, fullLink);
                pst.setString(idx, generated);
                pst.execute();
                connection.commit();
            } catch (SQLException e) {
                LOGGER.error("fullLink = " + fullLink, e);
                e.printStackTrace();
                connection.rollback();

            }
        } catch (SQLException e) {
            LOGGER.error("fullLink = " + fullLink, e);
            e.printStackTrace();
        }

        resp.getOutputStream().write("shortenLinkFromServlet".getBytes());
    }
}
