package com.example.junsta.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class ProfilesController {

    @Autowired
    private Environment env;

    @GetMapping("/profiles")
    public String getProfile(){
        return Arrays.stream(env.getActiveProfiles())
                .findFirst()
                .filter(e -> "set1".equals(e) || "set2".equals(e) || "test".equals(e))
                .orElse("");
    }

}
