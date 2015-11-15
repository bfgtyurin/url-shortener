package com.vtyurin.app.dao;

import com.vtyurin.app.model.Link;
import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class LinkDao {

    private static final Logger LOGGER = Logger.getLogger(LinkDao.class);

    @Autowired
    private JdbcConnectionPool dataSource;

    private static final String INSERT_STATEMENT = "INSERT INTO Links (clicks, fullUrl, shortUrl) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID_STATEMENT = "SELECT * FROM Links WHERE shortUrl=?";

    public LinkDao() {
    }

    public void persist(Link link) {
        Objects.requireNonNull(link);

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(INSERT_STATEMENT)) {
                int idx = 1;
                pst.setInt(idx++, link.getClicks());
                pst.setString(idx++, link.getFullUrl());
                pst.setString(idx, link.getShortUrl());
                pst.execute();
                LOGGER.info(link + " persisted");
            } catch (SQLException e) {
                LOGGER.error("Could not execute INSERT PreparedStatement", e);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Link getByShortUrl(String shortUrl) {
        Link link = new Link();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_ID_STATEMENT)) {
                pst.setString(1, shortUrl);
                ResultSet result = pst.executeQuery();
                result.next();
                link.setId(result.getInt(1));
                link.setClicks(result.getInt(2));
                link.setFullUrl(result.getString("fullUrl"));
                link.setShortUrl(result.getString("shortUrl"));
            } catch (SQLException e) {
                LOGGER.error("Could not execute SELECT PreparedStatement");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return link;
    }
}
