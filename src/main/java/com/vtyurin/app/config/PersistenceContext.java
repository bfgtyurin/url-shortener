package com.vtyurin.app.config;

import com.vtyurin.app.util.ConnectionUtil;
import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class PersistenceContext {
    private static Logger LOGGER = Logger.getLogger(ConnectionUtil.class);

    private static final String PROPERTY_JDBC_DRIVER = "jdbc.driverClassName";
    private static final String PROPERTY_JDBC_URL = "jdbc.url";
    private static final String PROPERTY_JDBC_USER = "jdbc.user";
    private static final String PROPERTY_JDBC_PASSWORD = "jdbc.password";

    @Bean
    JdbcConnectionPool dataSource(Environment env) {
        String driverName = env.getRequiredProperty(PROPERTY_JDBC_DRIVER);
        System.out.println(driverName);
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
