package com.example.junsta.posts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity uploadPost(@RequestBody @Valid PostRequestDto dto, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors.getFieldError());
        }

        PostResponseDto responseDto = postService.uploadPost(dto);
        return ResponseEntity.ok(responseDto);
    }
}
