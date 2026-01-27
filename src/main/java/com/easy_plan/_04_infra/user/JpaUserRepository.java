package com.easy_plan._04_infra.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByEmail(String value);

	boolean existsByNickname(String value);
	
	Optional<UserEntity> findByEmail(String email);
	
	Optional<UserEntity> findByNickname(String nickname);
	
	@Query("SELECT u.password FROM UserEntity u WHERE u.email = :email")
	String encodedPassword(String email);
}
