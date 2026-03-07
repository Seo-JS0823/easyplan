package com.easyplan._04_infra.persistence.orm.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaAuthRepository extends JpaRepository<AuthEntity, Long> {
	Optional<AuthEntity> findByUserId(Long userId);
	
	@Query("select e from AuthEntity e where e.userId = :userId")
	String getTokenHash(Long userId);
}
