package com.example.junsta.comments;

import com.example.junsta.accounts.Account;
import com.example.junsta.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Column(name = "CommentText",length = 1000)
    private String commentText;

    @ManyToOne(fetch = FetchType.LAZY)
    @CreatedBy
    private Account createdBy;

    @Builder
    public Comment(String commentText) {
        this.commentText = commentText;
    }
}
