package com.example.junsta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserVO {
	@JsonProperty(value="userId")
	private String userId;
	@JsonProperty(value="userName")
	private String userName;
	@JsonProperty(value="password")
	private String password;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
