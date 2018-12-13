package com.example.junsta.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.junsta.model.AuthUser;
import com.example.junsta.model.UserVO;

public interface UserService extends UserDetailsService {

	Collection<GrantedAuthority> getAuthorities(String username);

	public AuthUser readUser(String userId);

	public int createUser(AuthUser user);

	public int deleteUser(String userId);
	
	public int checkExists(String userId);

	public PasswordEncoder passwordEncoder();

	public UserVO getUserInfo(String userId);
}
