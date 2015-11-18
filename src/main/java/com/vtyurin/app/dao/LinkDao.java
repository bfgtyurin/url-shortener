package com.vtyurin.app.dao;

import com.vtyurin.app.model.Link;
import com.vtyurin.app.util.SequenceGenerator;
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

    public void persistWithTransaction(Link link) {
        Link newLink = new Link(link.getFullURL(), link.getShortURL(), link.getClicks());
        try (Connection connection = dataSource.getConnection()) {

            initializeIfFullUrlExist(connection, newLink);
            if (newLink.getId() == 0) {
                do {
                    newLink.setShortURL(SequenceGenerator.generate());
                } while (shortUrlExist(connection, newLink.getShortURL()));
                persist(connection, newLink);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeIfFullUrlExist(Connection connection, Link link) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_FULL_URL_STATEMENT)) {
            pst.setString(1, link.getFullURL());
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.isBeforeFirst()) {
                initializeLink(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not execute SELECT PreparedStatement");
        }
    }

    public boolean shortUrlExist(Connection connection, String shortURL) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_SHORT_URL_STATEMENT)) {
            pst.setString(1, shortURL);
            ResultSet resultSet = pst.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            LOGGER.error("Could not execute SELECT PreparedStatement");
        }

        return false;
    }

    private Link persist(Connection connection, Link link) throws SQLException {
        Objects.requireNonNull(link);
        try (PreparedStatement pst = connection.prepareStatement(INSERT_STATEMENT)) {
            int idx = 1;
            pst.setLong(idx++, link.getClicks());
            pst.setString(idx++, link.getFullURL());
            pst.setString(idx, link.getShortURL());
            pst.execute();
            ResultSet resultSet = pst.getGeneratedKeys();
            if (resultSet.next()) {
                link.setId(resultSet.getInt(1));
            }
            LOGGER.info(link + " persisted");
        } catch (SQLException e) {
            LOGGER.error("Could not execute INSERT PreparedStatement", e);
            connection.rollback();
        }

        return link;
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
                link = initializeLink(resultSet);
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
                link = initializeLink(resultSet);
            } catch (SQLException e) {
                LOGGER.error("Could not execute SELECT PreparedStatement");
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return link;
    }

    private Link initializeLink(ResultSet resultSet) throws SQLException {
        final Link link = new Link();
        if (resultSet.next()) {
            link.setId(resultSet.getInt(1));
            link.setClicks(resultSet.getInt(2));
            link.setFullURL(resultSet.getString("fullURL"));
            link.setShortURL(resultSet.getString("shortURL"));
        }

        return link;
    }
}
