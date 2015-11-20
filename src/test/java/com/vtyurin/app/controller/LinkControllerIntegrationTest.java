package com.vtyurin.app.controller;

import com.vtyurin.app.config.ApplicationContext;
import com.vtyurin.app.config.Profiles;
import com.vtyurin.app.dao.LinkDao;
import com.vtyurin.app.model.Link;
import org.h2.jdbcx.JdbcConnectionPool;
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
    JdbcConnectionPool dataSource;

    @Autowired
    LinkDao linkDao;

    Connection connection;

    @Before
    public void setUp() throws SQLException, FileNotFoundException {
        connection = dataSource.getConnection();
        FileReader fileReader = readSqlTestFile("sql/create-db-test.sql");
        loadSqlFromFile(fileReader);
    }

    @After
    public void tearDown() throws FileNotFoundException, SQLException {
        FileReader fileReader = readSqlTestFile("sql/drop-db-test.sql");
        loadSqlFromFile(fileReader);
    }

    private FileReader readSqlTestFile(String fileName) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return new FileReader(file);
    }

    private void loadSqlFromFile(FileReader fileReader) throws SQLException {
        RunScript.execute(connection, fileReader);
    }

    @Test
    public void testSaveLink() throws Exception {
        String fullUrl = "https://zxciop.com";
        Link link = new Link(fullUrl);
        linkController.saveLink(link, new MockHttpServletResponse());
        link = linkDao.getByFullURL(fullUrl);

        assertNotNull(link.getId());
        assertNotNull(link.getFullURL());
        assertNotNull(link.getShortURL());
    }

    @Test
    public void testSaveLinkShouldNotSave() throws Exception {
        String fullURL = "https://google.com";
        Link link = new Link(fullURL);
        linkController.saveLink(link, new MockHttpServletResponse());
        link = linkDao.getById(ID_AFTER_INSERT_ONE);

        assertNull(link.getId());
        assertEquals(0, link.getClicks());
        assertNull(link.getFullURL());
        assertNull(link.getShortURL());
    }

    @Test
    public void testSaveLinkShouldReturnExisting() throws Exception {
        String fullURL = "https://google.com";
        Link link = new Link(fullURL);
        linkController.saveLink(link, new MockHttpServletResponse());

        assertNotNull(link.getId());
        assertNotNull(link.getFullURL());
        assertNotNull(link.getShortURL());
    }

    @Test
    public void testShortUrlInvalid() {
        String shortURL = "12345aS";
        assertTrue(linkController.shortUrlInvalid(shortURL));
    }
}