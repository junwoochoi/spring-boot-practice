package com.example.junsta.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.junsta.model.PostVO;
import com.example.junsta.service.mapper.PostMapper;

@Service
public class PostServiceImpl implements PostService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PostMapper postMapper;

	@Override
	public List<PostVO> getAll() {
		
		return postMapper.getAll();
	}

}
