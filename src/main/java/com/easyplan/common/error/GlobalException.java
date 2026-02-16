package com.easyplan.common.error;

public class GlobalException extends RuntimeException {
	private final ErrorCode err;
	
	public GlobalException(ErrorCode err) {
		super(err.getMessage());
		this.err = err;
	}
	
	public GlobalException(ErrorCode err, Throwable cause) {
		super(err.getMessage(), cause);
		this.err = err;
	}
	
	public ErrorCode getErrorCode() {
		return err;
	}
}
