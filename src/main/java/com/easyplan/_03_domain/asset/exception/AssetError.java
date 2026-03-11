package com.easyplan._03_domain.asset.exception;

import com.easyplan.shared.exception.GlobalError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssetError implements GlobalError {
	
	UPDATE_ACCOUNT_DISABLED(400, "비활성화된 거래 항목입니다. 활성상태로 변경 후 수정해주세요."),
	
	UPDATE_SYSTEM_CATEGORY(400, "시스템 기본 계정은 수정할 수 없습니다."),
	
	ALREADY_EXISTS_SYSTEM_CATEGORY(400, "이미 시스템 기본 계정이 존재합니다."),
	
	
	
	;
	private final int status;
	
	private final String message;
}
