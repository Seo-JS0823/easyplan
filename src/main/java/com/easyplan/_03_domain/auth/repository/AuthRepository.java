package com.easyplan._03_domain.auth.repository;

import java.util.Optional;

import com.easyplan._03_domain.auth.model.Auth;

public interface AuthRepository {
	Optional<Auth> findById(Long id);
	
	Optional<Auth> findByUserId(Long userId);
	
	String getTokenHash(Long userId);
	
	Auth save(Auth auth);
	
	void updateTokenHash(Auth updateAuth);
}
