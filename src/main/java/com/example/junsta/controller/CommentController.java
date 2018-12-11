package com.example.junsta.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.junsta.model.CommentVO;
import com.example.junsta.service.CommentService;
import com.example.junsta.util.SessionUtil;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/comment")
public class CommentController {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CommentService commentService;
	
	@GetMapping("")
	public ResponseEntity<?> getComments(@RequestParam("postId") String postId){
		List<CommentVO> list = null;
		try {
			list = commentService.getCommentList(postId);
		} catch (Exception e) {
			logger.error(this.getClass().toGenericString() + e.getMessage());
			return new ResponseEntity<String>("쿼리 실행 실패", HttpStatus.BAD_GATEWAY);
		}
		if(list!=null) {
			return new ResponseEntity<List<CommentVO>>(list, HttpStatus.OK);
		}
		return new ResponseEntity<String>("실행 실패", HttpStatus.BAD_GATEWAY);
	}

	@PostMapping("/add")
	public ResponseEntity<?> addComment(@RequestBody CommentVO comment) {
		int checkAdded = -1;
		try {
			checkAdded = commentService.addComment(comment);
		} catch (Exception e) {
			logger.error(this.getClass().toGenericString() + e.getMessage());
			return new ResponseEntity<String>("쿼리 실행 실패", HttpStatus.BAD_GATEWAY);
		}
		if(checkAdded<0) {
			return new ResponseEntity<String>("쿼리 실행 실패", HttpStatus.BAD_GATEWAY);
		}
		return new ResponseEntity<CommentVO>(comment, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteComment(@RequestBody Map<String,String> map, HttpSession session){
		String postId = map.get("postId");
		String userId = SessionUtil.getUserInfo(session).getUserId();
		int check = -1;
		try{
			check = commentService.deleteComment(postId, userId);
			if(check == 0) {
				return new ResponseEntity<String>("존재하지않는 댓글 입니다.", HttpStatus.BAD_REQUEST);						
			} else if(check>0) {
				return new ResponseEntity<Map<String,String>>(map, HttpStatus.OK);										
			}
		} catch(Exception e) {
			logger.error(this.getClass() + e.getMessage());
		}
		return new ResponseEntity<String>("서버에 에러가 발생했습니다.", HttpStatus.BAD_GATEWAY);		
	}
}
