package com.example.junsta.likes;

import com.example.junsta.accounts.AccountAdapter;
import com.example.junsta.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity createLike(@RequestBody LikeDto dto
            , @AuthenticationPrincipal AccountAdapter accountAdapter) {

        likeService.createLike(dto.getPostId(), accountAdapter.getAccount());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity cancelLike(@RequestBody LikeDto dto
            , @AuthenticationPrincipal AccountAdapter accountAdapter) {

        likeService.cancelLike(dto.getPostId(), accountAdapter.getAccount());
        return ResponseEntity.noContent().build();
    }
}
