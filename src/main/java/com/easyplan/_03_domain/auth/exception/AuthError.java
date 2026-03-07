package com.easyplan._03_domain.auth.exception;

import com.easyplan.shared.exception.GlobalError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthError implements GlobalError {
	
	
	
	;
	private final int status;
	
	private final String message;
}
