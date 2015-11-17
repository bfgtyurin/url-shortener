package org.vtyurin.app.db;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContext.class})
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class LinkDaoTest {

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
        FileReader fileReader = new FileReader(file);
        return fileReader;
    }

    private void loadSqlFromFile(FileReader fileReader) throws SQLException {
        RunScript.execute(connection, fileReader);
    }

    @Test
    public void persistTest() throws SQLException {
        String fullURLValue = "http://site.com";
        String shortURLValue = "12345zX";
        Link link = new Link(fullURLValue, shortURLValue, 0);
        linkDao.persist(link);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Links WHERE fullUrl=?");
        preparedStatement.setString(1, fullURLValue);
        ResultSet resultSet = preparedStatement.executeQuery();
        assertEquals(resultSet.getFetchSize(), 0);
        assertTrue(resultSet.next());
        assertEquals(shortURLValue, resultSet.getString("shortUrl"));
    }

    @Test
    public void getByFullURLTest() {
        String fullURL = "https://google.com";
        Link link  = linkDao.getByFullURL(fullURL);
        assertEquals("12345aS", link.getShortURL());
        assertEquals(10, link.getClicks());
    }

    @Test
    public void getByShortURLTest() {
        String shortURL = "12345aS";
        Link link = linkDao.getByShortUrl(shortURL);
        assertEquals("https://google.com", link.getFullURL());
    }

    @Test
    public void getByShortUrlWithNotExistValue() {
        String shortURL = "mmmmmmm";
        Link link = linkDao.getByShortUrl(shortURL);
        assertNull(link.getFullURL());
        assertNull(link.getShortURL());
    }

    @Test
    public void updateTest() {
        String fullURL = "https://google.com";
        Link link = linkDao.getByFullURL(fullURL);
        long clicks = link.getClicks();
        link.setId(1);
        link.setClicks(clicks + 1);
        linkDao.update(link);
        link = linkDao.getByFullURL(fullURL);

        assertEquals(clicks + 1, link.getClicks());
    }

}
