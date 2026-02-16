package com.easyplan.infrastructure.persistence.auth.provider;

import com.easyplan.common.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtTokenErrorCode implements ErrorCode {
	EXPIRED(401, "TOKEN: 001", "SECURE: Expired Access Token"),
	
	MALFORMED(400, "TOKEN: 002", "지원하지 않는 인증 형식"),
	
	UN_SUPPORTED(401, "TOKEN: 003", "지원하지 않는 토큰 형식"),
	
	PARSE_ERROR(500, "TOKEN(E): 001", "인증 서버 에러 발생"),
	;
	private final int status;
	
	private final String code;
	
	private final String message;
}
