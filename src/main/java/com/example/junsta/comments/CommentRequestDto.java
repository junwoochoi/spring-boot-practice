package com.example.junsta.comments;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
public class CommentRequestDto {

    @NotEmpty
    private Long postId;

    @NotBlank
    private String commentText;



}
