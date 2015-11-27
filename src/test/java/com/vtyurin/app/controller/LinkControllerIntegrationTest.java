package com.vtyurin.app.controller;

import com.vtyurin.app.config.ApplicationContext;
import com.vtyurin.app.config.Profiles;
import com.vtyurin.app.dao.LinkDao;
import com.vtyurin.app.model.Link;
import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContext.class})
@WebAppConfiguration
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class LinkControllerIntegrationTest {

    private static final int ROWS_IN_LINKS_TABLE = 2;
    private static final int ID_AFTER_INSERT_ONE = ROWS_IN_LINKS_TABLE + 1;

    @Autowired
    LinkController linkController;

    @Autowired
    BasicDataSource dataSource;

    @Autowired
    LinkDao linkDao;

    @Before
    public void setUp() {
        try (Connection connection = dataSource.getConnection();
             FileReader fileReader = readSqlTestFile("sql/create-db-test.sql")) {
            RunScript.execute(connection, fileReader);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws FileNotFoundException, SQLException {
        try (Connection connection = dataSource.getConnection();
             FileReader fileReader = readSqlTestFile("sql/drop-db-test.sql")) {
            RunScript.execute(connection, fileReader);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private FileReader readSqlTestFile(String fileName) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return new FileReader(file);
    }

    @Test
    public void saveLinkShouldSave() throws Exception {
        String fullUrl = "https://zxciop.com";
        linkController.handlePostRequest(fullUrl, new MockHttpServletResponse());
        Link link = linkDao.getByFullUrl(fullUrl);

        assertNotNull(link.getId());
        assertNotNull(link.getFullUrl());
        assertNotNull(link.getShortUrl());
    }

    @Test
    public void saveLinkShouldNotSave() throws Exception {
        String fullURL = "https://google.com";
        linkController.handlePostRequest(fullURL, new MockHttpServletResponse());
        Link link = linkDao.getById(ID_AFTER_INSERT_ONE);

        assertNull(link.getId());
        assertEquals(0, link.getClicks());
        assertNull(link.getFullUrl());
        assertNull(link.getShortUrl());
    }

}