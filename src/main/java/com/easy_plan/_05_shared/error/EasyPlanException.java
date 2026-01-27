package com.easy_plan._05_shared.error;

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
