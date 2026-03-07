package com.easyplan._04_infra.persistence.orm.auth;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.easyplan._03_domain.auth.exception.AuthException;
import com.easyplan._03_domain.auth.model.Auth;
import com.easyplan._03_domain.auth.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {
	private final JpaAuthRepository authJpa;

	@Override
	public Optional<Auth> findById(Long id) {
		return authJpa.findById(id)
				.map(AuthEntity::toDomain);
	}

	@Override
	public Optional<Auth> findByUserId(Long userId) {
		return authJpa.findByUserId(userId)
				.map(AuthEntity::toDomain);
	}

	@Override
	public String getTokenHash(Long userId) {
		return authJpa.getTokenHash(userId);
	}

	@Override
	public Auth save(Auth auth) {
		AuthEntity entity = AuthEntity.create(auth);
		AuthEntity saved = authJpa.save(entity);
		return saved.toDomain();
	}

	@Override
	public void updateTokenHash(Auth updateAuth) {
		AuthEntity current = getForUpdate(updateAuth);
		
		current.updateTokenHash(updateAuth.getRefreshTokenHash().getValue());
		current.onUpdate(updateAuth.getUpdatedAt());
	}
	
	private AuthEntity getForUpdate(Auth auth) {
		return authJpa.findById(auth.getId())
				.orElseThrow(() -> new AuthException(AuthDataAccessError.INVALID_AUTH_INFO));
	}
	
	
	
	
}
