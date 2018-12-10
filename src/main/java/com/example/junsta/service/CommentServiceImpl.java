package com.example.junsta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.junsta.model.CommentVO;
import com.example.junsta.service.mapper.CommentMapper;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	CommentMapper mapper;
	
	public int addComment(CommentVO comment) {
		return mapper.insertComment(comment);
	}

	@Override
	public List<CommentVO> getCommentList(String postId) {
		return mapper.selectComments(postId);
	}
	
}
