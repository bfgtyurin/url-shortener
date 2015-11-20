package com.vtyurin.app.config;

import com.vtyurin.app.controller.HomeFilter;
import com.vtyurin.app.controller.LinkController;
import com.vtyurin.app.dao.LinkDao;
import org.springframework.context.annotation.*;

@Configuration
@Import({PersistenceContext.class})
public class ApplicationContext {

    @Profile(Profiles.APPLICATION)
    @Configuration
    @PropertySource("classpath:application.properties")
    static class ApplicationProperties {
    }

    @Profile(Profiles.INTEGRATION_TEST)
    @Configuration
    @PropertySource("classpath:integration-test.properties")
    static class IntegrationTestProperties {
    }

    @Bean
    LinkDao linkDao() {
        return new LinkDao();
    }

    @Bean
    HomeFilter homeFilter() {
        return new HomeFilter();
    }

    @Bean
    LinkController linkController() {
        return new LinkController();
    }
}

