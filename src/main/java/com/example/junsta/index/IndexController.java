package com.example.junsta.index;

import com.example.junsta.accounts.AccountController;
import com.example.junsta.posts.PostController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
public class IndexController {

    @GetMapping("/api")
    public @ResponseBody ResourceSupport index() {
        ResourceSupport index = new ResourceSupport();
        index.add(linkTo(PostController.class).withRel("Posts"));
        index.add(linkTo(AccountController.class).withRel("Accounts"));
        return index;
    }

    @GetMapping("/")
    public String docs(){
        return "/docs/index.html";
    }
}