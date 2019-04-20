package com.example.junsta.posts;

import com.example.junsta.comments.CommentResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {

    private Long id;
    private String imagePath;
    private String originalName;
    private String imageName;
    private String imageExtension;
    private String postText;
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
    private Integer likeCount;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.createdAt=post.getCreatedAt();
        this.createdBy=post.getAccount().getEmail();
        this.imageExtension=post.getUploadedImage().getImageExtension();
        this.imageName=post.getUploadedImage().getImageName();
        this.imagePath=post.getUploadedImage().getImagePath();
        this.originalName=post.getUploadedImage().getOriginalName();
        this.postText=post.getPostText();
        this.modifiedAt=post.getModifiedAt();
        this.likeCount=post.getLikeUsers().size();
    }
}
