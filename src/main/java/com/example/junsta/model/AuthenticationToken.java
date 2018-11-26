package com.example.junsta.model;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class AuthenticationToken {
	private String username;
	private String userId;
	private Collection authorities;
	private String token;

	public AuthenticationToken(String userId, String userName, Collection collection, String token) {
		this.username = userName;
		this.userId = userId;
		this.authorities = collection;
		this.token = token;
	}
}
