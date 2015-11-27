package com.vtyurin.app.dao;

import com.vtyurin.app.model.Link;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;

public class LinkDao {

    private static final Logger LOGGER = Logger.getLogger(LinkDao.class);

    private static final String SELECT_BY_ID = "SELECT * FROM links WHERE id=?";
    private static final String INSERT_STATEMENT = "INSERT INTO links (clicks, fullUrl, shortUrl, title) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_STATEMENT = "UPDATE links SET clicks = ? WHERE id = ?";
    private static final String SELECT_BY_FULL_URL_STATEMENT = "SELECT * FROM links WHERE fullURL=?";
    private static final String SELECT_BY_SHORT_URL_STATEMENT = "SELECT * FROM links WHERE shortUrl=?";

    @Autowired
    private BasicDataSource dataSource;

    public LinkDao() {
    }

    public void save(final Link link) {
        final String METHOD_NAME = "com.vtyurin.app.dao.LinkDao.save ";
        LOGGER.info(METHOD_NAME + "argument = " + link);

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {
                int idx = 1;
                preparedStatement.setLong(idx++, link.getClicks());
                preparedStatement.setString(idx++, link.getFullUrl());
                preparedStatement.setString(idx++, link.getShortUrl());
                preparedStatement.setString(idx, link.getTitle());
                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    link.setId(generatedKeys.getLong(1));
                    LOGGER.info(METHOD_NAME +"saved = " + link);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(METHOD_NAME + INSERT_STATEMENT, e);
        }
    }

    public Link getById(long id) {
        final String METHOD_NAME = "com.vtyurin.app.dao.LinkDao.getById ";
        LOGGER.info(METHOD_NAME + "argument = " + id);

        Link link = new Link();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_ID)) {
                pst.setLong(1, id);
                ResultSet resultSet = pst.executeQuery();
                link = initializeLink(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error(METHOD_NAME + SELECT_BY_ID, e);
        }

        LOGGER.info(METHOD_NAME + "return = " + link);
        return link;
    }

    public Link getByFullUrl(String fullUrl) {
        final String METHOD_NAME = "com.vtyurin.app.dao.LinkDao.getByFullUrl ";
        LOGGER.info(METHOD_NAME + "argument = " + fullUrl);

        Link link = new Link();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_FULL_URL_STATEMENT)) {
                pst.setString(1, fullUrl);
                ResultSet resultSet = pst.executeQuery();
                link = initializeLink(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error(METHOD_NAME + SELECT_BY_FULL_URL_STATEMENT, e);
        }

        LOGGER.info(METHOD_NAME + "return = " + link);
        return link;
    }

    public Link getByShortUrl(String shortUrl) {
        final String METHOD_NAME = "com.vtyurin.app.dao.LinkDao.getByShortUrl ";
        LOGGER.info(METHOD_NAME + "argument = " + shortUrl);

        Link link = new Link();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_SHORT_URL_STATEMENT)) {
                pst.setString(1, shortUrl);
                ResultSet resultSet = pst.executeQuery();
                link = initializeLink(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error(METHOD_NAME + SELECT_BY_SHORT_URL_STATEMENT, e);
        }

        LOGGER.info(METHOD_NAME + "return = " + link);
        return link;
    }

    public void update(Link link) {
        final String METHOD_NAME = "com.vtyurin.app.dao.LinkDao.update ";
        LOGGER.info(METHOD_NAME + "argument = " + link);

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(UPDATE_STATEMENT)) {
                int idx = 1;
                pst.setLong(idx++, link.getClicks());
                pst.setLong(idx, link.getId());
                pst.execute();
                LOGGER.info(METHOD_NAME + " updated = " + link);
            }
        } catch (SQLException e) {
            LOGGER.error(METHOD_NAME + UPDATE_STATEMENT + e);
        }
    }

    private Link initializeLink(ResultSet resultSet) throws SQLException {
        final String METHOD_NAME = "com.vtyurin.app.dao.LinkDao.initializeLink ";

        final Link link = new Link();
        if (resultSet.next()) {
            link.setId(resultSet.getInt("id"));
            link.setClicks(resultSet.getInt("clicks"));
            link.setFullUrl(resultSet.getString("fullURL"));
            link.setShortUrl(resultSet.getString("shortURL"));
            link.setTitle(resultSet.getString("title"));
        }

        LOGGER.info(METHOD_NAME + "return = " + link);
        return link;
    }

}
