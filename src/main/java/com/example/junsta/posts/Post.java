package com.example.junsta.posts;

import com.example.junsta.common.BaseEntity;
import com.example.junsta.uploadImages.UploadedImage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private UploadedImage uploadedImage;

    @Column(nullable = true)
    private String postText;

}
