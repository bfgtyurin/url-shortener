package com.vtyurin.app.dao;

import com.vtyurin.app.model.Link;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkDao {

    private static final Logger LOGGER = Logger.getLogger(LinkDao.class);

    private static final String SELECT_BY_ID = "SELECT * FROM Links WHERE id=?";
    private static final String INSERT_STATEMENT = "INSERT INTO Links (clicks, fullUrl, shortUrl) VALUES (?, ?, ?)";
    private static final String UPDATE_STATEMENT = "UPDATE Links SET clicks = ? WHERE id = ?";
    private static final String SELECT_BY_FULL_URL_STATEMENT = "SELECT * FROM Links WHERE fullURL=?";
    private static final String SELECT_BY_SHORT_URL_STATEMENT = "SELECT * FROM Links WHERE shortURL=?";

    @Autowired
    private BasicDataSource dataSource;

    public LinkDao() {
    }

    public void save(final Link link) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STATEMENT)) {
                int idx = 1;
                preparedStatement.setLong(idx++, link.getClicks());
                preparedStatement.setString(idx++, link.getFullUrl());
                preparedStatement.setString(idx, link.getShortUrl());
                preparedStatement.execute();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    link.setId(generatedKeys.getInt(1));
                }
                LOGGER.info(link + " saved");
            }
        } catch (SQLException e) {
            LOGGER.error("Could not execute INSERT PreparedStatement");
            e.printStackTrace();
        }
    }

    public Link getById(long id) {
        Link link = new Link();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_ID)) {
                pst.setLong(1, id);
                ResultSet resultSet = pst.executeQuery();
                link = initializeLink(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("Could not execute SELECT BY ID PreparedStatement");
        }

        return link;
    }

    public Link getByFullURL(String fullURL) {
        Link link = new Link();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_FULL_URL_STATEMENT)) {
                pst.setString(1, fullURL);
                ResultSet resultSet = pst.executeQuery();
                link = initializeLink(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("Could not execute SELECT BY FULL URL PreparedStatement");
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
            }
        } catch (SQLException e) {
            LOGGER.error("Could not execute SELECT BY SHORT URL PreparedStatement");
            e.printStackTrace();
        }

        return link;
    }

    public void update(Link link) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(UPDATE_STATEMENT)) {
                int idx = 1;
                pst.setLong(idx++, link.getClicks());
                pst.setLong(idx, link.getId());
                pst.execute();
                LOGGER.info(link + " updated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Link initializeLink(ResultSet resultSet) throws SQLException {
        final Link link = new Link();
        if (resultSet.next()) {
            link.setId(resultSet.getInt("id"));
            link.setClicks(resultSet.getInt("clicks"));
            link.setFullUrl(resultSet.getString("fullURL"));
            link.setShortUrl(resultSet.getString("shortURL"));
        }

        LOGGER.info("Initialized Link return = " + link);
        return link;
    }

}
