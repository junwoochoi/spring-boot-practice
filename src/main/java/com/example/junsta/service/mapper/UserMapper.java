package com.example.junsta.service.mapper;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.junsta.model.AuthUser;

public interface UserMapper {
	public List<AuthUser> selectUserList() throws Exception;
	public int createUser(AuthUser user) throws Exception;
	public int createAuthority(AuthUser user) throws Exception;
	public List<String> readAuthority(String userId);
	public AuthUser readUser(String userId);
	public int checkExists(String userId);
	public int deleteUser(String userId); 
	public int deleteAuthority(String userId); 
	public PasswordEncoder passwordEncoder();

}
