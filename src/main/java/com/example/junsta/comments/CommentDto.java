package com.example.junsta.comments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

    private String createdBy;
    private String commentText;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentDto(Comment comment){
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.createdBy = comment.getCreatedBy().getEmail();
        this.commentText = comment.getCommentText();
    }
}
