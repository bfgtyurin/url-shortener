package com.vtyurin.app.util;

import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class ConnectionUtil {

    private static Logger LOGGER = Logger.getLogger(ConnectionUtil.class);

    private static final String PROPERTY_JDBC_DRIVER = "jdbc.driverClassName";
    private static final String PROPERTY_JDBC_URL = "jdbc.url";
    private static final String PROPERTY_JDBC_USER = "jdbc.user";
    private static final String PROPERTY_JDBC_PASSWORD = "jdbc.password";

    @Autowired
    Environment env;

    /**
     * Creates a <code>JdbcConnectionPool</code> object
     * Get properties from @Autowired <code>Environment</code> variable.
     * Then initialize <code>JdbcConnectionPool</code> with these properties.
     *
     * @return a new <code>Connection</code> object from <code>JdbcConnectionPool</code>
     */
    public JdbcConnectionPool getDataSource() {
        String driverName = env.getRequiredProperty(PROPERTY_JDBC_DRIVER);
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Could not found jdbc Class Driver = " + driverName, e);
        }

        String url = env.getRequiredProperty(PROPERTY_JDBC_URL);
        String user = env.getRequiredProperty(PROPERTY_JDBC_USER);
        String password = env.getRequiredProperty(PROPERTY_JDBC_PASSWORD);
        JdbcConnectionPool pool = JdbcConnectionPool.create(url, user, password);

        return pool;
    }
}
