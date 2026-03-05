package com.easyplan.shared.exception;

@SuppressWarnings("serial")
public class GlobalException extends RuntimeException {

	private final GlobalError error;
	
	public GlobalException(GlobalError error) {
		super(error.getMessage());
		this.error = error;
	}
	
	public GlobalException(GlobalError error, Throwable cause) {
		super(error.getMessage(), cause);
		this.error = error;
	}
	
	public GlobalError getError() { return error; }
}
