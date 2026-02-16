package com.easyplan.domain.auth.error;

import com.easyplan.common.error.ErrorCode;
import com.easyplan.common.error.GlobalException;

public class AuthSessionException extends GlobalException {

	public AuthSessionException(ErrorCode err) {
		super(err);
	}
	
	public AuthSessionException(ErrorCode err, Throwable cause) {
		super(err, cause);
	}

}
