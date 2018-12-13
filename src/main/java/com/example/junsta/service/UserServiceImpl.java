package com.example.junsta.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.junsta.model.AuthUser;
import com.example.junsta.model.UserVO;
import com.example.junsta.service.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	UserMapper userMapper;
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 


	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		AuthUser user = userMapper.readUser(userId);
		user.setAuthorities(getAuthorities(userId));
		return user;

	}

	@Override
	public Collection<GrantedAuthority> getAuthorities(String userId) {
		List<String> string_authorities = userMapper.readAuthority(userId);
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String authority : string_authorities) {
			authorities.add(new SimpleGrantedAuthority(authority));
		}
		return authorities;
	}

	@Override
	public AuthUser readUser(String userId) {
		AuthUser user = userMapper.readUser(userId);
		user.setAuthorities(getAuthorities(userId));

		return user;
	}

	@Override
	public int createUser(AuthUser user) {
		int check = 0;
		String rawPassword = user.getPassword();
		String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
		user.setPassword(encodedPassword);
		try {
			check = userMapper.createUser(user);
			check += userMapper.createAuthority(user);
		} catch (Exception e) {
			logger.error("회원가입 실패 : >> {}", e.getMessage());
			return -1;
		}
		return check;

	}

	@Override
	public int deleteUser(String userId) {
		int check = 0;
		check = userMapper.deleteUser(userId);
		check += userMapper.deleteAuthority(userId);
		return check;
	}

	@Override
	public PasswordEncoder passwordEncoder() {
		return this.passwordEncoder;
	}

	@Override
	public int checkExists(String userId) {
		return userMapper.checkExists(userId);
	}

	@Override
	public UserVO getUserInfo(String userId) {
		
		return userMapper.getUserInfo(userId);
	}

}
