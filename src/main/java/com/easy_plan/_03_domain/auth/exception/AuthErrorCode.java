package com.easy_plan._03_domain.auth.exception;

import com.easy_plan._03_domain.EasyPlanErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements EasyPlanErrorCode {
	INVALID_TOKEN(401, "AUTH_01", "Security: [유효하지 않은 토큰입니다.]"),
	
	EXPIRED_TOKEN(401, "AUTH_02", "인증 정보가 만료되었습니다."),
	
	REFRESH_TOKEN_NOT_FOUND(401, "AUTH_03", "Security: [인증 정보를 찾을 수 없습니다.]"),
	
	TOKEN_BLACKLISTED(401, "AUTH_04", "Security: [이미 사용된 일회성 인증 정보입니다.]")
	
	
	;
	private final int status;
	private final String code;
	private final String message;
}
