package com.easyplan.domain.user.repository;

import com.easyplan.domain.user.model.Password;
import com.easyplan.domain.user.model.PasswordHash;

public interface PasswordHasher {
	PasswordHash encode(Password password);
	
	boolean matches(Password raw, PasswordHash encoded);
}
