package com.example.junsta;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SpringBootJunstaApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "/app/config/spring-boot-practice-sns/real-application.yml,"
            + "/app/config/spring-boot-practice-sns/aws.properties,";

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootJunstaApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}


