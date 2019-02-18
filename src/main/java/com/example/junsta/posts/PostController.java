package com.example.junsta.posts;

import com.example.junsta.accounts.AccountAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity getPost(Pageable pageable){
        Page<PostResponseDto> posts = postService.findAll(pageable);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity uploadPost(@RequestBody @Valid PostRequestDto dto, Errors errors,
                                     @AuthenticationPrincipal AccountAdapter accountAdapter){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors.getFieldError());
        }
        dto.setAccount(accountAdapter.getAccount());
        PostResponseDto responseDto = postService.uploadPost(dto);
        return ResponseEntity.ok(responseDto);
    }
}
