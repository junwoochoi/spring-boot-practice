package com.example.junsta.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.junsta.service.HashTagService;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/hashtag")
public class HashTagController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	HashTagService service;
	
	@GetMapping("/list")
	public ResponseEntity<?> getExistHashTag(@RequestParam("keyword") String keyword){
		List<Map<String,String>> resultMap = null;
		try {
			resultMap = service.findByKeyword(keyword);
			return new ResponseEntity<List<Map<String,String>>>(resultMap, HttpStatus.OK);
		} catch(Exception e) {
			logger.error(this.getClass() + e.getMessage());
			return new ResponseEntity<String>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
	}
		
}
