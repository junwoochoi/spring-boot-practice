package com.example.junsta.uploadImages;

import com.example.junsta.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadedImage extends BaseEntity {

    @Column(nullable = false, updatable = false)
    private String imagePath;

    @Column(nullable = false, updatable = false)
    private String originalName;

    @Column(nullable = false, updatable = false)
    private String imageName;

    @Column(nullable = false, updatable = false)
    private String imageExtension;

    @Builder
    public UploadedImage(String imagePath, String originalName, String imageName, String imageExtension) {
        this.imagePath = imagePath;
        this.originalName = originalName;
        this.imageName = imageName;
        this.imageExtension = imageExtension;
    }
}
