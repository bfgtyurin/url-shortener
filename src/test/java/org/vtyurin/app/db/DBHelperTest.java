package org.vtyurin.app.db;


import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DBHelperTest {

    @Test
    public void classForNameTest() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
    }

    @Test
    public void getConnectionPool() {
        JdbcConnectionPool pool = JdbcConnectionPool.create("jdbc:h2:tcp://localhost/~/test", "SA", "");
        assertNotNull(pool);
    }

    @Test
    public void getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
    }

    @Test
    public void getConnectionFromConnectionPool() throws SQLException {
        JdbcConnectionPool pool = JdbcConnectionPool.create("jdbc:h2:tcp://localhost/~/test", "SA", "");
        assertNotNull(pool.getConnection());
    }

    @Test
    public void insertTest() throws SQLException {
        JdbcConnectionPool pool = JdbcConnectionPool.create("jdbc:h2:tcp://localhost/~/test", "SA", "");
        Connection connection = pool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO LINKSTEST (fullLink, shortLink) VALUES ('testFullLinkValue', 'testShortLinkValue')");
        preparedStatement.execute();
        connection.commit();

        PreparedStatement selectPrepareStatment = connection.prepareStatement("SELECT * FROM LinksTest");
        ResultSet resultSet = selectPrepareStatment.executeQuery();
        assertTrue(resultSet.getFetchSize() > 0);
        while (resultSet.next()) {
            assertEquals("testFullLinkValue", resultSet.getString("fullLink"));
            assertEquals("testShortLinkValue", resultSet.getString("shortLink"));
        }
    }

}
