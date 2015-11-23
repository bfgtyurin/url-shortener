package com.vtyurin.app.config.db;

import com.vtyurin.app.config.Profiles;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Profile(Profiles.APPLICATION)
public class PostgresqlConfig {

    private static final Logger LOGGER = Logger.getLogger(PostgresqlConfig.class);

    @Bean
    public BasicDataSource dataSource() {
        URI dbUri = tryToGetURI();

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    private URI tryToGetURI() {
        URI dbURi = null;
        try {
            dbURi = getURI();
        } catch (URISyntaxException e) {
            LOGGER.error(e);
        }

        return dbURi;
    }

    private URI getURI() throws URISyntaxException {
        return new URI(System.getenv("DATABASE_URL"));
    }
}
