package com.example.junsta.uploadImages;

import com.example.junsta.common.BaseEntity;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
public class UploadedImage extends BaseEntity {

    @Column(nullable = false, updatable = false)
    private String imagePath;

    @Column(nullable = false, updatable = false)
    private String originalName;

    @Column(nullable = false, updatable = false)
    private String imageName;

    @Column(nullable = false, updatable = false)
    private String imageExtension;
}
