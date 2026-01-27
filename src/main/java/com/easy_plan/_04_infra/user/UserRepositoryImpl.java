package com.easy_plan._04_infra.user;

import org.springframework.stereotype.Repository;

import com.easy_plan._03_domain.user.UserRepository;
import com.easy_plan._03_domain.user.model.Email;
import com.easy_plan._03_domain.user.model.Nickname;
import com.easy_plan._03_domain.user.model.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final JpaUserRepository userRepo;
	
	@Override
	public boolean existsByEmail(Email email) {
		return userRepo.existsByEmail(email.getValue());
	}

	@Override
	public boolean existsByNickname(Nickname nickname) {
		return userRepo.existsByNickname(nickname.getValue());
	}

	@Override
	public User save(User user) {
		UserEntity entity = UserEntity.toEntity(user);
		UserEntity saved = userRepo.save(entity);
		
		return saved.toDomain();
	}
	
}
