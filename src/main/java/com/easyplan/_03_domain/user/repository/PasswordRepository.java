package com.easyplan._03_domain.user.repository;

import com.easyplan._03_domain.user.model.Password;
import com.easyplan._03_domain.user.model.PasswordHash;

public interface PasswordRepository {
	PasswordHash encode(Password password);
	
	boolean matches(Password rawPassword, PasswordHash encodedPassword);
}
