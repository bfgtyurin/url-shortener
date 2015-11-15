package com.vtyurin.app.config;

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
    LinkController linkController() {
        return new LinkController();
    }

    @Bean
    LinkDao linkDao() {
        return new LinkDao();
    }

}
