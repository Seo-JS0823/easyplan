package com.easy_plan._02_application.exception;

import com.easy_plan._03_domain.EasyPlanErrorCode;
import com.easy_plan._03_domain.EasyPlanException;

public class TokenBlacklistException extends EasyPlanException {

	public TokenBlacklistException(EasyPlanErrorCode error) {
		super(error);
	}
	
	public TokenBlacklistException(EasyPlanErrorCode error, Throwable cause) {
		super(error, cause);
	}

}
