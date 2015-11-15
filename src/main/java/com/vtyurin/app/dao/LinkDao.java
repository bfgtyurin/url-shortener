package com.vtyurin.app.dao;

import com.vtyurin.app.model.Link;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class LinkDao {

    private static final Logger LOGGER = Logger.getLogger(LinkDao.class);

    private Connection connection;
    private static final String INSERT_STATEMENT = "INSERT INTO Links (fullLink, shortLink) VALUES (?, ?)";

    public LinkDao(Connection connection) {
        this.connection = connection;
    }

    public void persist(Link link) {
        Objects.requireNonNull(link);

        try (PreparedStatement pst = connection.prepareStatement(INSERT_STATEMENT)) {
            int idx = 1;
            pst.setString(idx++, link.getFullUrl());
            pst.setString(idx, link.getShortUrl());
            pst.execute();
            LOGGER.info(link + " persisted");
        } catch (SQLException e) {
            LOGGER.error("Could not execute PreparedStatement", e);
            e.printStackTrace();
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
