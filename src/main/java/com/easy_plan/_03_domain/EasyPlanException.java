package com.easy_plan._03_domain;

public class EasyPlanException extends RuntimeException {
	private final EasyPlanErrorCode error;
	
	public EasyPlanException(EasyPlanErrorCode error) {
		super(error.getMessage());
		this.error = error;		
	}
	
	public EasyPlanException(EasyPlanErrorCode error, Throwable cause) {
		super(error.getMessage(), cause);
		this.error = error;
	}
	
	public EasyPlanErrorCode getError() {
		return error;
	}
}
