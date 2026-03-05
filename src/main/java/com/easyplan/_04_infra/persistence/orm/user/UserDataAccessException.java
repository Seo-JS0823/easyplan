package com.easyplan._04_infra.persistence.orm.user;

import com.easyplan.shared.exception.GlobalError;
import com.easyplan.shared.exception.GlobalException;

public class UserDataAccessException extends GlobalException {

	public UserDataAccessException(GlobalError error) {
		super(error);
	}
	
	public UserDataAccessException(GlobalError error, Throwable cause) {
		super(error, cause);
	}
}
