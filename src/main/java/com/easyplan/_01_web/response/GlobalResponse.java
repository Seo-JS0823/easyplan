package com.easyplan._01_web.response;

import org.springframework.http.ResponseEntity;

import lombok.Getter;

@Getter
public class GlobalResponse<T> {
	private final int status;
	
	private final boolean success;
	
	private final String message;
	
	private final T data;
	
	private GlobalResponse(int status, boolean success, String message, T data) {
		this.status = status;
		this.success = success;
		this.message = message;
		this.data = data;
	}
	
	private static <T> GlobalResponse<T> successResponse(String message, T data) {
		return new GlobalResponse<>(200, true, message, data);
	}
	
	private static <T> GlobalResponse<T> failResponse(String message, T data) {
		return new GlobalResponse<>(400, false, message, data);
	}
	
	public static <T> GlobalResponse<T> success() {
		return successResponse("success", null);
	}
	
	public static <T> ResponseEntity<GlobalResponse<T>> successEntity() {
		return ResponseEntity.ok(successResponse("success", null));
	}
	
	public static <T> GlobalResponse<T> success(String message) {
		return successResponse(message, null);
	}
	
	public static <T> ResponseEntity<GlobalResponse<T>> successEntity(String message) {
		return ResponseEntity.ok(successResponse(message, null));
	}
	
	public static <T> GlobalResponse<T> success(String message, T data) {
		return successResponse(message, data);
	}
	
	public static <T> ResponseEntity<GlobalResponse<T>> successEntity(String message, T data) {
		return ResponseEntity.ok(successResponse(message, data));
	}
	
	public static <T> ResponseEntity<GlobalResponse<T>> failEntity() {
		return ResponseEntity.ok(failResponse("fail", null));
	}
}
