package com.example.junsta.service;

import java.util.List;

import com.example.junsta.model.CommentVO;

public interface CommentService {
	public int addComment(CommentVO comment);
	public List<CommentVO> getCommentList(String postId);
	public int deleteComment(String postId, String userId);
}
