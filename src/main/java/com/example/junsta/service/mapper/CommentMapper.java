package com.example.junsta.service.mapper;

import java.util.List;
import java.util.Map;

import com.example.junsta.model.CommentVO;

public interface CommentMapper {
	public int insertComment(CommentVO comment);
	public List<CommentVO> selectComments(String postId);
	public int deleteComment(Map<String,String> map);
}