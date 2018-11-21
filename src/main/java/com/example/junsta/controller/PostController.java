package com.example.junsta.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.example.junsta.model.PostVO;
import com.example.junsta.service.PostService;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/post")
public class PostController {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PostService postService;

	@GetMapping("/all")
	public ResponseEntity<?> getAll(HttpSession session) {
		List<PostVO> postList;
		postList = postService.getAll();
		if(postList!=null) {			
			return new ResponseEntity<List<PostVO>>(postList, HttpStatus.OK);
		}
		return new ResponseEntity<String>("게시글 받아오기 실패", HttpStatus.BAD_REQUEST);	
	}
	
	@GetMapping("/image/{id}")
	public ResponseEntity<?> getImage(@PathVariable("id") String id) {
	    byte[] image;
		try {
			image = postService.getImage(id);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
		} catch (IOException e) {			
			logger.error("이미지 받아오기 실패 >> ");
			logger.error(e.getMessage());
			return new ResponseEntity<String>("이미지 받아오기 실패", HttpStatus.BAD_REQUEST);		
		}
	}
	

	@PostMapping("/upload")
	public ResponseEntity<?> uploadPost(final WebRequest webRequest,
			@RequestParam("fileContent") final MultipartFile fileContent, @RequestParam("inputJson") String post) {

		try {
			postService.uploadPost(post, fileContent);
		} catch (IOException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>( "파일을 확인하세요.", HttpStatus.BAD_REQUEST);
		}

		logger.info("데이터 삽입 성공");
		return new ResponseEntity<String>("데이터 삽입 성공", HttpStatus.OK);

	}

}
