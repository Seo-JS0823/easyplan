package com.easy_plan._03_domain.user.exception;

import com.easy_plan._03_domain.EasyPlanErrorCode;
import com.easy_plan._03_domain.EasyPlanException;

public class UserException extends EasyPlanException {

	public UserException(EasyPlanErrorCode error) {
		super(error);
	}
	
	public UserException(EasyPlanErrorCode error, Throwable cause) {
		super(error, cause);
	}
	
}
