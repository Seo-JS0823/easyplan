package com.easyplan._03_domain.user.exception;

import com.easyplan.shared.exception.GlobalError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserValueError implements GlobalError {
	INVALID_PUBLIC_ID(401, "올바른 유저 식별자가 아닙니다."),
	
	INVALID_EMAIL(400, "올바른 이메일 형식이 아닙니다."),
	
	INVALID_NICKNAME(400, "올바른 닉네임 형식이 아닙니다."),
	
	INVALID_PASSWORD(400, "올바른 비밀번호 형식이 아닙니다."),
	
	INVALID_PASSWORD_HASH(400, "지원하는 비밀번호 해시 형식이 아닙니다."),
	
	
	;
	private final int status;
	
	private final String message;
}
