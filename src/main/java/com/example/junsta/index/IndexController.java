package com.example.junsta.index;

import com.example.junsta.accounts.AccountController;
import com.example.junsta.posts.PostController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/api")
    public ResourceSupport index() {
        ResourceSupport index = new ResourceSupport();
        index.add(linkTo(PostController.class).withRel("Posts"));
        index.add(linkTo(AccountController.class).withRel("Accounts"));
        return index;
    }
}