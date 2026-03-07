package com.easyplan._03_domain.auth.exception;

import com.easyplan.shared.exception.GlobalError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthError implements GlobalError {
	
	INVALID_USER_ID(400, "인증 정보를 생성할 회원 정보가 불확실함"),
	
	AUTH_NOT_FOUND(400, "인증 정보가 존재하지 않음"),
	
	AUTH_SESSION_NOT_MATCH(401, "저장된 인증 정보와 일치하지 않음"),
	
	USED_REFRESH_TOKEN(401, "이미 사용된 인증 정보"),
	
	USED_ACCESS_TOKEN(401, "이미 사용된 인증 정보"),
	
	;
	private final int status;
	
	private final String message;
}
