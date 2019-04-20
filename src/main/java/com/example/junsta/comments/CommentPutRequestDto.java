package com.example.junsta.comments;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class CommentPutRequestDto {

    @NotNull
    private Long commentId;
    @NotBlank
    private String commentText;
}
