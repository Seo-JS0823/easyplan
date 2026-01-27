package com.easy_plan._03_domain;

/*
 * 인증 관련 Error는 Security: 식별자를 넣어서 ValidErrorCode 설계
 * 
 * 로그에는 잘 찍히나 사용자에게는 인증 오류 라는 응답만 줌
 */
public class ValidErrorCode implements EasyPlanErrorCode {

	private final String message;
	
	private final Object object;
	
	private int status = 400;
	
	private String code = "BAD_REQUEST";
	
	public ValidErrorCode(String message, Object object) {
		this.message = message;
		this.object = object;
	}
	
	public ValidErrorCode(int status, String message, Object object) {
		this.status = status;
		this.message = message;
		this.object = object;
	}
	
	public ValidErrorCode(int status, String message, Object object, String code) {
		this.status = status;
		this.code = code;
		this.message = message;
		this.object = object;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public String getMessage() {
		return "[" + name() + "] " + message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String name() {
		return (object instanceof String s)
				? s
				: object.getClass().getSimpleName();
	}
}
