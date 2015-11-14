package org.vtyurin.app.db;


import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;

public class DBHelperTest {

    @Test
    public void classForNameTest() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
    }

    @Test
    public void getConnectionPool() {
        JdbcConnectionPool pool = JdbcConnectionPool.create("jdbc:h2:tcp://localhost//data/test", "SA", "");
        assertNotNull(pool);
    }

    @Test
    public void getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost//data/test", "sa", "");
    }

    @Test
    public void getConnectionFromConnectionPool() throws SQLException {
        JdbcConnectionPool pool = JdbcConnectionPool.create("jdbc:h2:tcp://localhost//data/test", "SA", "");
        assertNotNull(pool.getConnection());
    }

}
