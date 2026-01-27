package com.easy_plan._03_domain;

public class ValidException extends EasyPlanException {
	public ValidException(EasyPlanErrorCode error) {
		super(error);
	}
	
	public ValidException(EasyPlanErrorCode error, Throwable cause) {
		super(error, cause);
	}
}
