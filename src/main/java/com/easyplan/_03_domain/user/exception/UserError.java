package com.easyplan._03_domain.user.exception;

import com.easyplan.shared.exception.GlobalError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserError implements GlobalError {
	
	USER_DISABLED(400, "비활성화된 계정입니다."),
	
	USER_SIGNED_ERROR(400, "회원가입 대상의 식별자가 부여되기 전 이미 생성 되어있습니다."),
	;
	private final int status;
	
	private final String message;
}
