package com.example.junsta.posts;

import com.example.junsta.uploadImages.UploadedImage;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @NotEmpty @NotBlank(message = "imagePath should not be blank")
    private String imagePath;
    @NotEmpty @NotBlank(message = "originalName should not be blank")
    private String originalName;
    @NotEmpty @NotBlank(message = "imageName should not be blank")
    private String imageName;
    @NotEmpty @NotBlank(message = "imageExtension should not be blank")
    private String imageExtension;
    @NotEmpty @NotBlank(message = "postText should not be blank")
    private String postText;

    public UploadedImage getUploadImageEntity() {
        return UploadedImage.builder()
                .imagePath(imagePath)
                .originalName(originalName)
                .imageName(imageName)
                .imageExtension(imageExtension)
                .build();
    }

    public Post getPostEntity(){
        return Post.builder()
                .uploadedImage(getUploadImageEntity())
                .postText(postText)
                .build();
    }
}
