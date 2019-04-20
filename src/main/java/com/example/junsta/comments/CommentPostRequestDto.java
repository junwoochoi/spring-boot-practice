package com.example.junsta.comments;

import com.example.junsta.accounts.Account;
import com.example.junsta.exceptions.PostNotExistException;
import com.example.junsta.posts.PostService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class CommentPostRequestDto {

    @NotNull
    private Long postId;

    @NotBlank
    private String commentText;

    public Comment toEntity(Account currentUser, PostService postService) {
        return Comment.builder()
                .post(postService.findById(this.postId).orElseThrow(PostNotExistException::new))
                .account(currentUser)
                .commentText(this.commentText)
                .build();
    }
}
