package com.easyplan.infrastructure.persistence.auth.provider;

import com.easyplan.common.error.ErrorCode;
import com.easyplan.common.error.GlobalException;

public class JwtTokenException extends GlobalException {

	public JwtTokenException(ErrorCode err, Throwable cause) {
		super(err, cause);
	}

}
