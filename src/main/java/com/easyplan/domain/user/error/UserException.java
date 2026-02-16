package com.easyplan.domain.user.error;

import com.easyplan.common.error.ErrorCode;
import com.easyplan.common.error.GlobalException;

public class UserException extends GlobalException {

	public UserException(ErrorCode err) {
		super(err);
	}

	public UserException(ErrorCode err, Throwable cause) {
		super(err, cause);
	}
}
