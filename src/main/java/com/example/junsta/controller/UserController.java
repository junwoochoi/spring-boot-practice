package com.example.junsta.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.junsta.model.ApiResponseMessage;
import com.example.junsta.model.AuthUser;
import com.example.junsta.model.AuthenticationRequest;
import com.example.junsta.model.AuthenticationToken;
import com.example.junsta.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/user")
public class UserController {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;
	@Autowired
	AuthenticationManager authenticationManager;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest, HttpSession session) {
		String userId = authenticationRequest.getUserId();
		String password = authenticationRequest.getPassword();

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userId, password);

		try {
			Authentication authentication = authenticationManager.authenticate(token);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());

			AuthUser user = userService.readUser(userId);
			logger.info("{} 로그인 ", userId);
			AuthenticationToken authToken = new AuthenticationToken(user.getUserId(), user.getAuthorities(),
					session.getId());
			return new ResponseEntity<AuthenticationToken>(authToken, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(userId + "로그인 에러 : >> "+e.getMessage());
			ApiResponseMessage response = new ApiResponseMessage(ApiResponseMessage.FAILED, "", "LOGIN0001", "아이디와 비번을 확인하십시오.");
			return new ResponseEntity<ApiResponseMessage>(response, HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponseMessage> create(@RequestBody AuthUser user) throws JsonProcessingException {

		int check = userService.createUser(user);
		String result = "";
		if (check > 0) {
			result = "회원가입 완료";
			ApiResponseMessage responseMessage = new ApiResponseMessage(ApiResponseMessage.SUCCESS, result, "", "");
			return new ResponseEntity<ApiResponseMessage>(responseMessage, HttpStatus.OK);
		}
		result = "회원가입 실패";
		ApiResponseMessage responseMessage = new ApiResponseMessage(ApiResponseMessage.FAILED, "", "LOGIN0000", result);
		return new ResponseEntity<ApiResponseMessage>(responseMessage, HttpStatus.BAD_REQUEST);
	}

}
