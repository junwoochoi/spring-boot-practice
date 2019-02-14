package com.example.junsta.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties(prefix = "my-app")
@Getter @Setter
public class AppProperties {

    @NotEmpty
    private String clientId;

    @NotEmpty
    private String secret;

    @NotEmpty
    private String testEmail;

    @NotEmpty
    private String testPassword;

}
