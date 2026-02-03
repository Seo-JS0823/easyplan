package com.easy_plan._04_infra.auth;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.easy_plan._03_domain.auth.AuthRepository;
import com.easy_plan._03_domain.auth.model.Auth;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {
	private final JpaAuthRepository authRepo;
	
	@Override
	public Auth saveAuth(Auth auth) {
		AuthEntity entity = AuthEntity.from(auth);
		AuthEntity saved = authRepo.save(entity);
		return saved.toDomain();
	}

	@Override
	public Optional<Auth> findAuthByUserId(Long userId) {
		return authRepo.findByUserId(userId)
				.map(AuthEntity::toDomain);
	}

	@Override
	public void deleteAuth(Auth auth) {
		authRepo.deleteById(auth.getAuthId());
	}

	@Override
	public void deleteAuthFlush(Auth auth) {
		authRepo.deleteById(auth.getAuthId());
		authRepo.flush();
	}
	
}
