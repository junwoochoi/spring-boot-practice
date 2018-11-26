package com.example.junsta.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.example.junsta.model.PostVO;
import com.example.junsta.service.PostService;
import com.example.junsta.util.JsonUtil;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/post")
public class PostController {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PostService postService;
	
	@GetMapping("/checkAuth")
	public ResponseEntity<?> checkAuth(){
		return new ResponseEntity<String>("권한있음", HttpStatus.OK);
	}
	
	@GetMapping("/like/check")
	public ResponseEntity<?> checkLike(HttpSession session, @RequestParam("userId")String userId,@RequestParam("postId")String postId){
		Map<String,String> map = new HashMap<String,String>();
		map.put("postId", postId);
		map.put("userId", userId);
		
		logger.info("파라미터 map : >> {}", map);
		Boolean checkLike = null;
		try {
			checkLike = postService.getLike(map);			
			return new ResponseEntity<Boolean>(checkLike, HttpStatus.OK);
		} catch (Exception e) {	
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);					
		}
	}
	
	@PostMapping("/like/toggle")
	public ResponseEntity<?> toggleLike(@RequestBody Map<String,String> map){
		int check = -1;
		  if(postService.getLike(map)) {
			  check = postService.deleteLike(map);
		  } else {
			  check = postService.insertLike(map); 
		  }
			if(check<1) {				
				return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);		
			} 
			return new ResponseEntity<String>("좋아요 토글 성공", HttpStatus.OK);
	}
	

	@GetMapping("/all")
	public ResponseEntity<?> getAll(HttpSession session, @RequestParam("startIndex")Integer startIndex, @RequestParam("limit")Integer limit ) {
		List<PostVO> postList;
		Map<String, Integer> pageMap = new HashMap<String, Integer>();
		pageMap.put("startIndex", startIndex);
		pageMap.put("limit", limit);
		postList = postService.getAll(pageMap);
		if (postList != null) {
			return new ResponseEntity<List<PostVO>>(postList, HttpStatus.OK);
		}
		return new ResponseEntity<String>("게시글 받아오기 실패", HttpStatus.BAD_GATEWAY);
	}

	@PostMapping("/image")
	public ResponseEntity<?> getImage(@RequestBody Map<String,String> map) {
		byte[] image;
		try {
			String fileName = map.get("fileName");
			image = postService.getImage(fileName);
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
			return new ResponseEntity<String>("파일을 확인하세요.", HttpStatus.BAD_REQUEST);
		}

		logger.info("데이터 삽입 성공");
		return new ResponseEntity<String>("데이터 삽입 성공", HttpStatus.OK);

	}

	
}
