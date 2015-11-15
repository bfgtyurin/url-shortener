package org.vtyurin.app.db;

import com.vtyurin.app.dao.LinkDao;
import com.vtyurin.app.model.Link;
import com.vtyurin.app.util.ConnectionUtil;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LinkDaoTest {

    LinkDao linkDao;
    Connection connection;

    @Before
    public void setUp() throws SQLException, FileNotFoundException {
        linkDao = new LinkDao(ConnectionUtil.getTestConnection());
        connection = ConnectionUtil.getTestConnection();
        FileReader fileReader  = readSqlTestFile();
        loadSqlFromFile(fileReader);
    }

    private FileReader readSqlTestFile() throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sql/test.sql").getFile());
        FileReader fileReader  = new FileReader(file);
        return fileReader;
    }

    private void loadSqlFromFile(FileReader fileReader) throws SQLException {
            RunScript.execute(connection, fileReader);
    }

    @Test
    public void persistTest() throws SQLException {
        String fullLinkValue = "http://site.com";
        String shortLinkValue = "12345zX";
        Link link = new Link(fullLinkValue, shortLinkValue, 0);
        linkDao.persist(link);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Links WHERE fullLink=?");
        preparedStatement.setString(1, fullLinkValue);
        ResultSet resultSet = preparedStatement.executeQuery();
        assertEquals(resultSet.getFetchSize(), 0);
        assertTrue(resultSet.next());
        assertEquals(shortLinkValue, resultSet.getString("shortLink"));
    }

}
