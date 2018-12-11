package com.example.junsta.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.junsta.service.mapper.HashTagMapper;

@Service
public class HashTagServiceImpl implements HashTagService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	HashTagMapper mapper;

	@Override
	public List<Map<String, String>> findByKeyword(String keyword) {

		return mapper.searchByKeyword(keyword);

	}

}
