package com.easyplan.domain.auth.error;

import com.easyplan.common.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthSessionErrorCode implements ErrorCode {
	INVALID_TOKEN_ID(401, "SECURE: TOKEN_ID", "SECURE: 유효하지 않은 토큰 식별자"),
	
	INVALID_TOKEN_UNSUPPORTED(401, "SECURE: TOKEN_ID", "SECURE: 지원하지 않는 토큰 아이디 형식"),
	
	INVALID_TOKEN_HASH(401, "SECURE: REFRESH_TOKEN", "SECURE: 존재하지 않는 토큰 해시"),
	
	INVALID_ACCESS_TOKEN(401, "SECURE: ACCESS_TOKEN", "SECURE: 유효하지 않은 인증 토큰"),
	
	INVALID_ACCESS_TOKEN_UNSUPPORTED(401, "SECURE: ACCESS_TOKEN", "SECURE: 지원하지 않는 인증 토큰"),
	
	INVALID_REFRESH_TOKEN(401, "SECURE: REFRESH_TOKEN", "SECURE: 유효하지 않은 리프레시 토큰"),
	
	INVALID_REFRESH_TOKEN_UNSUPPORTED(401, "SECURE: REFRESH_TOKEN", "SECURE: 지원하지 않는 리프레시 토큰"),
	
	USED_ACCESS_TOKEN(401, "SECURE: USED_ACCESS", "SECURE: 이미 사용된 액세스 토큰으로 요청이 들어왔습니다."),
	
	USED_REFRESH_TOKEN(401, "SECURE: USED_REFRESH", "SECURE: 이미 사용된 리프레시 토큰으로 요청이 들어왔습니다."),
	
	
	;
	private final int status;
	
	private final String code;
	
	private final String message;
}
