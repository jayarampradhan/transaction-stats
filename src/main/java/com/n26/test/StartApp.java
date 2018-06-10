package com.n26.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class StartApp  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        new StartApp()
                .configure(new SpringApplicationBuilder(StartApp.class))
                .run(args);
    }

}
