package com.example.junsta.model;

import java.sql.Blob;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostVO {
	private int postId;
	private String uploadBy;
	private Blob contents_pic;
	private String contents_text;
	private String uploadDate;
}
