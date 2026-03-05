package com.easyplan._04_infra.persistence.orm.user;

import com.easyplan.shared.exception.GlobalError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserDataAccessError implements GlobalError {
	USER_NOT_FOUND(400, "존재하지 않는 사용자"),
	
	;
	private final int status;
	
	private final String message;
}
