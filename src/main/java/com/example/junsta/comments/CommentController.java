package com.example.junsta.comments;

import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountAdapter;
import com.example.junsta.common.PageableValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final PageableValidator pageableValidator;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity createComment(@RequestBody @Valid CommentPostRequestDto dto, Errors errors,
                                        @AuthenticationPrincipal AccountAdapter accountAdapter) {
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        Account currentUser = accountAdapter.getAccount();

        return ResponseEntity.ok(commentService.saveComment(dto, currentUser));
    }

    @GetMapping
    public ResponseEntity getComments(Long postId, Pageable pageable){
        pageableValidator.validate(pageable);
        return ResponseEntity.ok(commentService.findByPostId(postId, pageable));
    }

    @PutMapping
    public ResponseEntity updateComment(@RequestBody @Valid CommentPutRequestDto dto, Errors errors,
                                        @AuthenticationPrincipal AccountAdapter accountAdapter){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        Account currentUser = accountAdapter.getAccount();
        return ResponseEntity.ok(commentService.updateComment(dto, currentUser));
    }

}

