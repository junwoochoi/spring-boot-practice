package com.example.junsta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.junsta.service.PostService;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/comment")
public class CommentController {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PostService postService;

	@GetMapping("/checkAuth")
	public ResponseEntity<?> checkAuth() {
		return new ResponseEntity<String>("권한있음", HttpStatus.OK);
	}
}
