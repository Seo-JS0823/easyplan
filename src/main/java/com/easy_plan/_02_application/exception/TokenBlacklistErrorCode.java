package com.easy_plan._02_application.exception;

import com.easy_plan._03_domain.EasyPlanErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenBlacklistErrorCode implements EasyPlanErrorCode {
	BLACKLIST_INVALID_ACCESS_TOKEN(401, "ERROR_CODE=1", "Security: [액세스 토큰 재사용 공격 감지]"),
	BLACKLIST_INVALID_REFRESH_TOKEN(401, "ERROR_CODE=2", "Security: [리프레시 토큰 재사용 공격 감지]");
	
	private final int status;
	
	private final String code;
	
	private final String message;
}
