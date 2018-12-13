package com.example.junsta.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.junsta.model.AuthUser;
import com.example.junsta.model.AuthenticationRequest;
import com.example.junsta.model.AuthenticationToken;
import com.example.junsta.model.UserVO;
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

	@GetMapping("/myInfo")
	public ResponseEntity<?> getMyInfo(HttpSession session) {
		SecurityContext context = (SecurityContext) session
				.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		if (context != null) {
			Authentication auth = context.getAuthentication();
			if (auth != null) {
				return ResponseEntity.ok(auth.getDetails());
			}
		}
		return ResponseEntity.badRequest().build();

	}

	@GetMapping("/exists")
	public ResponseEntity<Map<String, Boolean>> checkExists(@RequestParam("userId") String userId) {
		int check = -1;
		check = userService.checkExists(userId);
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if (check != -1) {

			if (check == 0) {
				map.put("exists", false);
			} else if (check > 0) {
				map.put("exists", true);
			}
			return ResponseEntity.ok(map);
		}

		return ResponseEntity.badRequest().build();

	}

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
			AuthenticationToken authToken = new AuthenticationToken(user.getUserId(), user.getUsername(),
					user.getAuthorities(), session.getId());
			return new ResponseEntity<AuthenticationToken>(authToken, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(userId + "로그인 에러 : >> " + e.getMessage());
			return new ResponseEntity<String>("아이디와 비번을 확인하세요", HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<String> create(@RequestBody AuthUser user) throws JsonProcessingException {

		int check = -1;
		check = userService.createUser(user);
		String result = "";

		logger.info("check 값 : {}", check);
		if (check > 0) {
			result = "회원가입 완료";
			return new ResponseEntity<String>(result, HttpStatus.OK);
		}
		if (check == -1) {
			return new ResponseEntity<String>("이미존재하는 회원", HttpStatus.BAD_REQUEST);
		} else {
			result = "회원가입 실패";
			return new ResponseEntity<String>(result, HttpStatus.BAD_GATEWAY);
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/userInfo")
	public ResponseEntity<?> getInfo(@RequestParam("userId")String userId){
		UserVO user= null;
		try{
			user = userService.getUserInfo(userId);
		} catch(Exception e) {
			return new ResponseEntity<String>("데이터베이스 에러", HttpStatus.BAD_GATEWAY);
		}
		if(user==null) {
			return new ResponseEntity<String>("존재하지않는 회원입니다.", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<UserVO>(user, HttpStatus.OK);
		
	}
}
