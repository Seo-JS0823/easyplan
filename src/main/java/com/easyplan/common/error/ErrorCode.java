package com.easyplan.common.error;

public interface ErrorCode {
	int getStatus();
	
	String getCode();
	
	String getMessage();
}
