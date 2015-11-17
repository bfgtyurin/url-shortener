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
    private static final String UPDATE_STATEMENT = "UPDATE Links SET clicks = ? WHERE id = ?";
    private static final String SELECT_BY_FULL_URL_STATEMENT = "SELECT * FROM Links WHERE fullURL=?";
    private static final String SELECT_BY_SHORT_URL_STATEMENT = "SELECT * FROM Links WHERE shortURL=?";

    public LinkDao() {
    }

    public void persist(Link link) {
        Objects.requireNonNull(link);

        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);
            try (PreparedStatement pst = connection.prepareStatement(INSERT_STATEMENT)) {
                int idx = 1;
                pst.setLong(idx++, link.getClicks());
                pst.setString(idx++, link.getFullURL());
                pst.setString(idx, link.getShortURL());
                pst.execute();

                connection.commit();
                LOGGER.info(link + " persisted");
            } catch (SQLException e) {
                LOGGER.error("Could not execute INSERT PreparedStatement", e);
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void update(Link link) {

        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);
            try (PreparedStatement pst = connection.prepareStatement(UPDATE_STATEMENT)) {
                pst.setLong(1, link.getClicks());
                pst.setLong(2, link.getId());
                pst.execute();

                connection.commit();
                LOGGER.info(link + " updated");
            } catch (SQLException e) {
                LOGGER.error("Could not execute INSERT PreparedStatement", e);
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Link getByFullURL(String fullURL) {
        Link link = new Link();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_FULL_URL_STATEMENT)) {
                pst.setString(1, fullURL);
                ResultSet resultSet = pst.executeQuery();
                initializeLink(link, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Could not execute SELECT PreparedStatement");
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return link;
    }

    public Link getByShortUrl(String shortURL) {
        Link link = new Link();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_SHORT_URL_STATEMENT)) {
                pst.setString(1, shortURL);
                ResultSet resultSet = pst.executeQuery();
                initializeLink(link, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Could not execute SELECT PreparedStatement");
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return link;
    }

    private void initializeLink(Link link, ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            link.setId(resultSet.getInt(1));
            link.setClicks(resultSet.getInt(2));
            link.setFullURL(resultSet.getString("fullURL"));
            link.setShortURL(resultSet.getString("shortURL"));
        }
    }
}
