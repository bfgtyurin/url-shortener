package com.vtyurin.app.db;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class DBHelper {

    private static final String JDBC_ULR = "jdbc:h2:tcp://localhost/~/test";

    JdbcConnectionPool pool;

    public DBHelper() {
        init();
    }

    public static DBHelper getInstance() {
        return new DBHelper();
    }

    private void init() {
        pool = JdbcConnectionPool.create(JDBC_ULR, "SA", "");
    }

    public Connection getConnection() throws SQLException {

        return pool.getConnection();
    }
}
