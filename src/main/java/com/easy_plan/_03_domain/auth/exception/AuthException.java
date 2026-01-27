package com.easy_plan._03_domain.auth.exception;

import com.easy_plan._03_domain.EasyPlanErrorCode;
import com.easy_plan._03_domain.EasyPlanException;

public class AuthException extends EasyPlanException {
	public AuthException(EasyPlanErrorCode error) {
		super(error);
	}
	
	public AuthException(EasyPlanErrorCode error, Throwable cause) {
		super(error, cause);
	}
}
