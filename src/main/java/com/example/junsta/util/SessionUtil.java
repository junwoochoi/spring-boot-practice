package com.example.junsta.util;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.example.junsta.model.AuthUser;

public class SessionUtil {

	public static AuthUser getUserInfo(HttpSession session) {
		SecurityContext context = (SecurityContext) session
				.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		AuthUser userInfo = (AuthUser) context.getAuthentication().getPrincipal();

		return userInfo;
	}
}
