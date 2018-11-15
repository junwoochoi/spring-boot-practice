package com.example.junsta.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ApiResponseMessage {
	private String status;
	private String message;
	private String errorMessage;
	private String errorCode;
	
	public static final String SUCCESS = "success";
	public static final String FAILED = "failed";
	
	public ApiResponseMessage() {}
	
	/**
	 * 
	 * @param status		상태 success / failed
	 * @param message		메시지
	 * @param errorMessage	에러메시지
	 * @param errorCode		에러코드
	 */
	public ApiResponseMessage(String status, String message, String errorCode, String errorMessage) {
		this.status = status;
		this.message = message;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
}
