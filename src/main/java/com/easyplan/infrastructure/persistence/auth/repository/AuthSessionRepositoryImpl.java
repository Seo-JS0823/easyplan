package com.easyplan.infrastructure.persistence.auth.repository;

import org.springframework.stereotype.Repository;

import com.easyplan.domain.auth.model.AuthSession;
import com.easyplan.domain.auth.model.Subject;
import com.easyplan.domain.auth.repository.AuthSessionRepository;
import com.easyplan.infrastructure.persistence.auth.entity.AuthSessionEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthSessionRepositoryImpl implements AuthSessionRepository {
	private final JpaAuthSessionRepository authRepo;

	@Override
	public AuthSession save(AuthSession authSession) {
		String subject = authSession.getSubject();
		
		AuthSessionEntity entity = authRepo.findBySubject(subject)
				.map(existing -> {
					existing.apply(authSession);
					return existing;
				})
				.orElseGet(() -> AuthSessionEntity.create(authSession));
		
		AuthSessionEntity saved = authRepo.save(entity);
		
		return saved.toDomain();
	}

	@Override
	public AuthSession findBySubject(Subject subject) {
		AuthSessionEntity entity = authRepo.findBySubject(subject.userPublicId().getValue())
				.orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않음"));
		
		return entity.toDomain();
	}
	
}
