package com.example.junsta.posts;

import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRequestValidator postRequestValidator;

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

        postRequestValidator.validateUploadedImage(dto, accountAdapter.getAccount());

        PostResponseDto responseDto = postService.uploadPost(dto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping
    public ResponseEntity updatePost(@RequestBody @Valid PostUpdateRequestDto dto, Errors errors,
                                     @AuthenticationPrincipal AccountAdapter accountAdapter){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors.getFieldError());
        }

        PostResponseDto responseDto = postService.updatePost(dto, accountAdapter.getAccount());
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping
    public ResponseEntity deletePost(@RequestBody PostDeleteRequestDto dto,
                                     @AuthenticationPrincipal AccountAdapter accountAdapter){
        Account currentUser = accountAdapter.getAccount();


        postService.deletePost(dto.getId(), currentUser);

        return ResponseEntity.noContent().build();
    }
}

