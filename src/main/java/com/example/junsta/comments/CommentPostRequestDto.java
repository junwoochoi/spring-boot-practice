package com.example.junsta.comments;

import com.example.junsta.accounts.Account;
import com.example.junsta.exceptions.CommentNotExistException;
import com.example.junsta.exceptions.PostNotExistException;
import com.example.junsta.posts.PostService;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long parentCommentId;

    public Comment toEntity(PostService postService, CommentRepository commentRepository) {
        if(parentCommentId == null){
            return Comment.builder()
                    .post(postService.findById(this.postId).orElseThrow(PostNotExistException::new))
                    .commentText(this.commentText)
                    .build();
        }
        return Comment.builder()
                .post(postService.findById(this.postId).orElseThrow(PostNotExistException::new))
                .parentComment(commentRepository.findById(parentCommentId).orElseThrow(CommentNotExistException::new))
                .commentText(this.commentText)
                .build();
    }
}
