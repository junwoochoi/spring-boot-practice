package com.example.junsta.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource(value = "file://C:/Users/JUNU/IdeaProjects/junsta/dbProperties.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file://C:/Users/JUNU/IdeaProjects/junsta/dbProperties.properties", ignoreResourceNotFound = true)
})
@Getter
public class GlobalPropertyConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


}
