package com.easyplan._02_application.error;

import com.easyplan.common.error.ErrorCode;
import com.easyplan.common.error.GlobalException;

public class UserAppException extends GlobalException {

	public UserAppException(ErrorCode err) {
		super(err);
	}
	
	public UserAppException(ErrorCode err, Throwable cause) {
		super(err, cause);
	}

}
