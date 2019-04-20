package com.example.junsta.likes;

import com.example.junsta.accounts.AccountAdapter;
import com.example.junsta.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity createLike(@RequestBody Long postId
            , @AuthenticationPrincipal AccountAdapter accountAdapter) {

        postService.createLike(postId, accountAdapter.getAccount());
        return ResponseEntity.noContent().build();
    }
}
