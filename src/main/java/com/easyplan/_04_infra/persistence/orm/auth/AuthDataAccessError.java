package com.easyplan._04_infra.persistence.orm.auth;

import com.easyplan.shared.exception.GlobalError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthDataAccessError implements GlobalError {
	
	// Jwt Error
	EXPIRED(401, "JWT: ExpiredJwtException"),
	
	UNSUPPORTED(401, "JWT: UnsupportedJwtException"),
	
	MALFORMED(401, "JWT: MalformedJwtException"),
	
	PARSING_ERROR(500, "JWT: parsing error"),
	
	// Persistence Error
	INVALID_AUTH_INFO(401, "AUTH: 인증 정보가 존재하지 않음"),
	;
	private final int status;
	
	private final String message;
}
