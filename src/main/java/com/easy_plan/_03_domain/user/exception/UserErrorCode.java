package com.easy_plan._03_domain.user.exception;

import com.easy_plan._03_domain.EasyPlanErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements EasyPlanErrorCode {
	ALREADY_EMAIL(409, "USER_01", "이미 사용중인 이메일 입니다."),
	ALREADY_NICKNAME(409, "USER_02", "이미 사용중인 닉네임 입니다."),
	SIGNUP_ERROR(500, "USER_03", "회원가입중 알 수 없는 오류가 발생했습니다."),
	USER_NOT_FOUND(400, "USER_04", "사용자를 찾을 수 없습니다."),
	LOGIN_NOT_MATCH(400, "USER_05", "로그인 정보가 일치하지 않습니다.");
	
	private final int status;
	
	private final String code;
	
	private final String message;
}
