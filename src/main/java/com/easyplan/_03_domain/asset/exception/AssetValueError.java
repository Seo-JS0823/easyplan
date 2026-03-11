package com.easyplan._03_domain.asset.exception;

import com.easyplan.shared.exception.GlobalError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssetValueError implements GlobalError {
	NOT_FOUND_CATEGORY_NAME(400, "거래 카테고리 이름을 지정하세요."),
	INVALID_CATEGORY_NAME(400, "거래 카테고리 이름은 10자 이내로 지정하세요."),
	
	NOT_FOUND_ACCOUNT_NAME(400, "거래 항목 이름을 지정하세요."),
	INVALID_ACCOUNT_NAME(400, "거래 항목 이름은 20자 이내로 지정하세요."),
	
	NOT_FOUND_CATEGORY(400, "계정 항목이 배치될 카테고리가 존재하지 않습니다."),
	
	
	
	;
	private final int status;
	
	private final String message;
}
