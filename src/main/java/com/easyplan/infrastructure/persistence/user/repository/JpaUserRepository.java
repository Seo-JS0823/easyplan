package com.easyplan.infrastructure.persistence.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.easyplan.infrastructure.persistence.user.entity.UserEntity;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByPublicId(String publicId);
	
	boolean existsByEmail(String email);
	
	boolean existsByNickname(String nickname);

	Optional<UserEntity> findByEmail(String value);
	
	@Query("select u.password from UserEntity u where u.publicId = :publicId")
	String getPasswordHash(String publicId);
}
