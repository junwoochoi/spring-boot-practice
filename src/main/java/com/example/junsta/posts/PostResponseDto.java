package com.example.junsta.posts;

import com.example.junsta.comments.CommentResponseDto;
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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();

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
        if(post.getComments()!=null){
            this.commentList= post.getComments().stream().map(comment -> new CommentResponseDto(comment)).collect(Collectors.toList());
        }
    }
}
