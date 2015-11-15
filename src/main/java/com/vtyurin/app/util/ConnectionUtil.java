package com.vtyurin.app.util;

import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionUtil {

    private static Logger LOGGER = Logger.getLogger(ConnectionUtil.class);

    private static final String PROD_DB_CONNECTION = "prod-db-connection";
    private static final String TEST_DB_CONNECTION = "test-db-connection";

    private static final String PROPERTY_JDBC_URL = "jdbc.url";
    private static final String PROPERTY_JDBC_DRIVER = "jdbc.driverClassName";
    private static final String PROPERTY_JDBC_USER = "jdbc.user";
    private static final String PROPERTY_JDBC_PASSWORD = "jdbc.password";

    private static JdbcConnectionPool pool;

    /** Creates a <code>ResourceBundle</code> object
     * based on <code>PROD_DB_CONNECTION</code> property file.
     * Then initialize <code>JdbcConnectionPool</code> with these properties.
     * @return a new <code>Connection</code> object from <code>JdbcConnectionPool</code>
     */
    public static Connection getProdConnection() {
        ResourceBundle resource = ResourceBundle.getBundle(PROD_DB_CONNECTION);
        return createConnection(resource);
    }

    /** Creates a <code>ResourceBundle</code> object
     * based on <code>TEST_DB_CONNECTION</code> property file.
     * Then initialize <code>JdbcConnectionPool</code> with these properties.
     * @return a new <code>Connection</code> object from <code>JdbcConnectionPool</code>
     */
    public static Connection getTestConnection() {
        ResourceBundle resource = ResourceBundle.getBundle(TEST_DB_CONNECTION);
        return createConnection(resource);
    }

    private static Connection createConnection(ResourceBundle resource) {
        createConnectionPool(resource);
        Connection connection = null;
        try {
            connection = pool.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Could not get connection from Connection Pool", e);
            e.printStackTrace();
        }

        return connection;
    }

    private static void createConnectionPool(ResourceBundle resource) {
        String driverName = resource.getString(PROPERTY_JDBC_DRIVER);
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Could not found jdbc Class Driver = " + driverName, e);
        }

        String url = resource.getString(PROPERTY_JDBC_URL);
        String user = resource.getString(PROPERTY_JDBC_USER);
        String password = resource.getString(PROPERTY_JDBC_PASSWORD);
        pool = JdbcConnectionPool.create(url, user, password);
    }
}
