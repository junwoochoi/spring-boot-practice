package com.example.junsta.uploadImages;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadedImageDto {

    private String imagePath;
    private String originalName;
    private String imageName;
    private String imageExtension;


    @Builder
    public UploadedImageDto(String imagePath, String originalName, String imageName, String imageExtension) {
        this.imagePath = imagePath;
        this.originalName = originalName;
        this.imageName = imageName;
        this.imageExtension = imageExtension;
    }

    public UploadedImageDto(UploadedImage image){
        this.imageExtension = image.getImageExtension();
        this.originalName = image.getOriginalName();
        this.imageName = image.getImageName();
        this.imagePath = image.getImagePath();
    }

}
