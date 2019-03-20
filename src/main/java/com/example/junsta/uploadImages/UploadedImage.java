package com.example.junsta.uploadImages;

import com.example.junsta.accounts.Account;
import com.example.junsta.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "upload_image")
public class UploadedImage extends BaseEntity {

    @Column(name = "image_path", nullable = false, updatable = false)
    private String imagePath;

    @Column(name = "original_name", nullable = false, updatable = false)
    private String originalName;

    @Column(name = "image_name", nullable = false, updatable = false)
    private String imageName;

    @Column(name = "image_extension", nullable = false, updatable = false)
    private String imageExtension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @CreatedBy
    private Account createdBy;

    @Builder
    public UploadedImage(String imagePath, String originalName, String imageName, String imageExtension) {
        this.imagePath = imagePath;
        this.originalName = originalName;
        this.imageName = imageName;
        this.imageExtension = imageExtension;
    }

}
