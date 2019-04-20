package com.example.junsta.comments;

import com.example.junsta.accounts.Account;
import com.example.junsta.common.BaseEntity;
import com.example.junsta.posts.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Column(name = "comment_text", length = 1000)
    private String commentText;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName= "id")
    @CreatedBy
    private Account createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Builder
    public Comment(String commentText, Post post, Account account) {
        this.commentText = commentText;
        this.post = post;
        this.createdBy = account;
    }

    public void updateText(CommentPutRequestDto dto) {
        this.commentText = dto.getCommentText();
    }
}
