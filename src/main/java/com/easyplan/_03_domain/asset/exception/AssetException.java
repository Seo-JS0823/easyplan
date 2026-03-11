package com.easyplan._03_domain.asset.exception;

import com.easyplan.shared.exception.GlobalError;
import com.easyplan.shared.exception.GlobalException;

public class AssetException extends GlobalException {

	public AssetException(GlobalError error) {
		super(error);
	}

	public AssetException(GlobalError error, Throwable cause) {
		super(error, cause);
	}
}
