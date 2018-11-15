package com.example.junsta.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.junsta.model.AuthUser;
import com.example.junsta.model.PostVO;
import com.example.junsta.service.PostService;
import com.example.junsta.util.SessionUtil;


@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/post")
public class PostController {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PostService postService;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAll(HttpSession session) {
		List<PostVO> postList = postService.getAll();
		return new ResponseEntity<>(postList, HttpStatus.ACCEPTED);
	}
	 
	@PostMapping("/upload")
	public ResponseEntity<?> uploadPost(HttpSession session, PostVO post){
		
		
		return new ResponseEntity<AuthUser>(SessionUtil.getUserInfo(session), HttpStatus.OK);
		
	}
	

}
