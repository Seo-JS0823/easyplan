package com.easyplan._03_domain.auth.exception;

import com.easyplan.shared.exception.GlobalError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthValueError implements GlobalError {
	INVALID_SUBJECT(401, "SECURE: Jwt subject invalid"),
	
	INVALID_JWT(401, "SECURE: Jwt jti invalid"),
	
	INVALID_EXPIRATION(401, "SECURE: Jwt Expiration invalid"),
	
	INVALID_ACCESS_TOKEN(401, "SECURE: Jwt Access Token invalid"),
	
	INVALID_REFRESH_TOKEN(401, "SECURE: Refresh Token invalid"),
	
	INVALID_REFRESH_TOKEN_HASH(401, "SECURE: Refresh Token Hash invalid"),
	
	INVALID_TOKEN_PAIR(500, "SECURE: Token Pair invalid"),
	
	
	;
	private final int status;
	
	private final String message;
}
