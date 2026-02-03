package com.easy_plan._03_domain.auth;

import java.util.Optional;

import com.easy_plan._03_domain.auth.model.Auth;

public interface AuthRepository {
	Auth saveAuth(Auth auth);
	
	Optional<Auth> findAuthByUserId(Long userId);
	
	void deleteAuth(Auth auth);
	
	void deleteAuthFlush(Auth auth);
}
