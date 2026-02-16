package com.easyplan._02_application.error;

import com.easyplan.common.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserAppErrorCode implements ErrorCode {
	CHANGE_SAME_PASSWORD(400, "APP: 001", "변경하려는 비밀번호가 현재 비밀번호와 동일합니다."),
	
	CHANGE_SAME_NICKNAME(400, "APP: 002", "변경하려는 닉네임이 현재 닉네임과 동일합니다."),
	
	
	
	;
	private final int status;
	
	private final String code;
	
	private final String message;
}
