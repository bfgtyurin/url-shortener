package com.vtyurin.app.config;

import com.vtyurin.app.controller.HomeFilter;
import com.vtyurin.app.controller.LinkController;
import com.vtyurin.app.dao.LinkDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.vtyurin.app.config.db")
public class ApplicationContext {

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

