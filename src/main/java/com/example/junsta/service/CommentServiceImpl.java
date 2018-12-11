package com.example.junsta.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override
	public int deleteComment(String postId, String userId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("postId", postId);
		return mapper.deleteComment(map);
	}
	
}
