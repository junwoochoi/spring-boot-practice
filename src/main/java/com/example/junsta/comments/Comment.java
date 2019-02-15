package com.example.junsta.comments;

import com.example.junsta.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    private String commentText;

    @Builder
    public Comment(String commentText) {
        this.commentText = commentText;
    }
}
