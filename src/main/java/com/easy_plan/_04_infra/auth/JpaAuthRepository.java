package com.easy_plan._04_infra.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAuthRepository extends JpaRepository<AuthEntity, Long> {
	Optional<AuthEntity> findByJti(String jti);
	
	Optional<AuthEntity> findByUserId(Long userId);
}
