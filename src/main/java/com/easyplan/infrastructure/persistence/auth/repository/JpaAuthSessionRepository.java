package com.easyplan.infrastructure.persistence.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyplan.infrastructure.persistence.auth.entity.AuthSessionEntity;

public interface JpaAuthSessionRepository extends JpaRepository<AuthSessionEntity, String> {
	Optional<AuthSessionEntity> findBySubject(String subject);
}
