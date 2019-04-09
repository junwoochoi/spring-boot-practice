package com.example.junsta.comments;

import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity createComment(@RequestBody @Valid CommentRequestDto dto, Errors errors,
                                        @AuthenticationPrincipal AccountAdapter accountAdapter) {
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        Account currentUser = accountAdapter.getAccount();

        return ResponseEntity.ok(commentService.saveComment(dto, currentUser));
    }

}

