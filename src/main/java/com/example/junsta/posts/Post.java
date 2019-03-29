package com.example.junsta.posts;

import com.example.junsta.accounts.Account;
import com.example.junsta.comments.Comment;
import com.example.junsta.common.BaseEntity;
import com.example.junsta.uploadImages.UploadedImage;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "post")
public class Post extends BaseEntity {

    @JoinColumn(name = "upload_image_id", nullable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UploadedImage uploadedImage;

    @Column(name = "post_text", nullable = true)
    private String postText;


    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("createdAt ASC")
    private List<Comment> comments;

    @CreatedBy
    @JoinColumn(name = "created_by", referencedColumnName= "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY, optional = false)
    private Account account;

    @Builder
    public Post(UploadedImage uploadedImage, String postText, List<Comment> comments, Account account) {
        this.uploadedImage = uploadedImage;
        this.postText = postText;
        this.comments = comments;
        this.account = account;
    }


    public void updateText(PostUpdateRequestDto dto) {
        if (dto.getId() == this.getId()) {
            this.postText = dto.getPostText();
        }
    }
}
