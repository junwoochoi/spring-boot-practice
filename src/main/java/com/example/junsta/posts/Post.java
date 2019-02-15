package com.example.junsta.posts;

import com.example.junsta.accounts.Account;
import com.example.junsta.comments.Comment;
import com.example.junsta.common.BaseEntity;
import com.example.junsta.uploadImages.UploadedImage;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Post extends BaseEntity {

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private UploadedImage uploadedImage;

    @Column(nullable = true)
    private String postText;

    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    private List<Comment> comments;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Builder
    public Post(UploadedImage uploadedImage, String postText, List<Comment> comments) {
        this.uploadedImage = uploadedImage;
        this.postText = postText;
        this.comments = comments;
    }


}
