package com.easy_plan._04_infra.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByEmail(String value);

	boolean existsByNickname(String value);
}
