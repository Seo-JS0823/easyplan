package com.easyplan._04_infra.persistence.orm.user;

import org.springframework.stereotype.Repository;

import com.easyplan._03_domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final JpaUserRepository userJpa;
	
}
