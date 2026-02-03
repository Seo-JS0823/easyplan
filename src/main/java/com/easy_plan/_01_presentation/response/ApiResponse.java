package com.easy_plan._01_presentation.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
	private final boolean success;
	
	private final int status;
	
	private final String message;
	
	private final T data;
	
	public static <T> ApiResponse<T> success(String message) {
		return new ApiResponse<>(true, 200, message, null);
	}
	
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(true, 200, message, data);
	}
	
	public static <T> ApiResponse<T> fail(int status, String message) {
		return new ApiResponse<>(false, status, message, null);
	}
	
	public static <T>	 ApiResponse<T> fail(int status, String message, T data) {
		return new ApiResponse<>(false, status, message, data);
	}
}
