package com.easyplan.domain.user.error;

import com.easyplan.common.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
	
	USER_NOT_FOUND(400, "USER: 001", "계정정보가 일치하지 않습니다."),
	
	DUPLICATE_EMAIL(409, "USER: 002", "이미 존재하는 사용자입니다."),
	
	DUPLICATE_NICKNAME(409, "USER: 003", "이미 사용중인 닉네임입니다."),
	
	USER_DISABLED(400, "USER: 004", "비활성화된 계정입니다."),
	
	INVALID_EMAIL(400, "USER-V: 001", "올바르지 않은 이메일 형식"),
	
	INVALID_PASSWORD(400, "USER-V: 002", "올바르지 않은 패스워드 형식"),
	
	INVALID_PASSWORD_HASH(400, "USER-V: 003", "올바르지 않은 패스워드 해시"),
	
	INVALID_NICKNAME(400, "USER-V: 004", "올바르지 않은 닉네임 형식"),
	
	INVALID_PUBLIC_ID(400, "USER-V: 005", "사용자 식별값이 올바르지 않음"),
	;	
	private final int status;
	
	private final String code;
	
	private final String message;
}