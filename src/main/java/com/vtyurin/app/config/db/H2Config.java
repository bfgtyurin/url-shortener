package com.vtyurin.app.config.db;

import com.vtyurin.app.config.Profiles;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@Profile(Profiles.INTEGRATION_TEST)
@PropertySource("classpath:integration-test.properties")
public class H2Config {
    private static Logger LOGGER = Logger.getLogger(H2Config.class);

    private static final String PROPERTY_JDBC_DRIVER = "jdbc.driverClassName";
    private static final String PROPERTY_JDBC_URL = "jdbc.url";
    private static final String PROPERTY_JDBC_USER = "jdbc.user";
    private static final String PROPERTY_JDBC_PASSWORD = "jdbc.password";

    @Bean
    BasicDataSource dataSource(Environment env) {
        String driverName = env.getRequiredProperty(PROPERTY_JDBC_DRIVER);
        System.out.println(driverName);
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Could not found jdbc Class Driver = " + driverName, e);
        }

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(env.getRequiredProperty(PROPERTY_JDBC_URL));
        dataSource.setUsername(env.getRequiredProperty(PROPERTY_JDBC_USER));
        dataSource.setPassword(env.getRequiredProperty(PROPERTY_JDBC_PASSWORD));

        return dataSource;
    }
}

