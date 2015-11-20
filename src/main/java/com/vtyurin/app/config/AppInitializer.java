package com.vtyurin.app.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.*;
import java.util.EnumSet;

public class AppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(new ContextLoaderListener(getContext()));

        FilterRegistration.Dynamic homeFilter = servletContext.addFilter("homeFilter", DelegatingFilterProxy.class);
        homeFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        ServletRegistration.Dynamic linkController = servletContext.addServlet("linkController", HttpRequestHandlerServlet.class);
        linkController.addMapping("/shorten");
    }

    private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(ApplicationContext.class);
        ctx.getEnvironment().setActiveProfiles(Profiles.APPLICATION);

        return ctx;
    }
}
