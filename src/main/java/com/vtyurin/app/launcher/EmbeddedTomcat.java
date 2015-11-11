package com.vtyurin.app.launcher;

import java.io.File;

import org.apache.catalina.startup.Tomcat;

public class EmbeddedTomcat {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.addWebapp("/", new File("src/main/webapp").getAbsolutePath());
        tomcat.start();
        tomcat.getServer().await();
    }

}
