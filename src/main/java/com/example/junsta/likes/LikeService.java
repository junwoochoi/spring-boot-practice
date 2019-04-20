package com.example.junsta.likes;

import com.example.junsta.accounts.Account;
import com.example.junsta.posts.Post;
import com.example.junsta.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostService postService;

    public void createLike(Long postId, Account currentUser) {
        Post post = postService.findPostIfExists(postId, currentUser);

        post.createLike(currentUser);
    }

    public void cancelLike(Long postId, Account currentUser) {
        Post post = postService.findPostIfExists(postId, currentUser);

        post.cancelLike(currentUser);
    }
}
