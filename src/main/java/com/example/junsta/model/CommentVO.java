package com.example.junsta.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CommentVO {
	private String comment_text;
	private String commentId;
	private String uploadDate;
	private String postId;
	private String writerId;
}
