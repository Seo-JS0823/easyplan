package com.easyplan._03_domain.user.exception;

import com.easyplan.shared.exception.GlobalError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserError implements GlobalError {
	
	USER_DISABLED(400, "비활성화된 계정입니다."),
	
	USER_SIGNED_ERROR(400, "회원가입 대상의 식별자가 부여되기 전 이미 생성 되어있습니다."),
	
	DUPLICATE_EMAIL(409, "이미 사용중인 아이디입니다."),
	
	DUPLICATE_NICKNAME(409, "이미 사용중인 닉네임입니다."),
	
	USER_NOT_FOUND(400, "존재하지 않는 사용자입니다."),
	
	LOGIN_NOT_MATCHES(400, "입력하신 아이디와 패스워드가 일치하지 않습니다."),
	
	
	;
	private final int status;
	
	private final String message;
}
