package com.easy_plan._04_infra.user;

import java.time.Instant;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.easy_plan._03_domain.user.UserRepository;
import com.easy_plan._03_domain.user.exception.UserErrorCode;
import com.easy_plan._03_domain.user.exception.UserException;
import com.easy_plan._03_domain.user.model.Email;
import com.easy_plan._03_domain.user.model.Nickname;
import com.easy_plan._03_domain.user.model.User;
import com.easy_plan._05_shared.Clock;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final JpaUserRepository userRepo;
	
	private final PasswordEncoder passwordEncoder;
	
	private final Clock clock;
	
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
		Instant now = clock.now();
		user.encodePassword(passwordEncoder.encode(user.getPassword().getValue()));
		user.createdAt(now);
		user.updatedAt(now);
		UserEntity entity = UserEntity.toEntity(user);
		UserEntity saved;
		
		try {
			saved = userRepo.save(entity);
		} catch (DataIntegrityViolationException e) {
			// DataIntegrityViolationException
			// Unique, Private Key, 중복 불가 제약, 알 수 없는 에러 등에 걸리면 UserException으로 예왜 매핑
			throw new UserException(UserErrorCode.SIGNUP_ERROR, e);
		}
		return saved.toDomain();
	}

	@Override
	public Optional<User> findByEmail(Email email) {
		return userRepo.findByEmail(email.getValue())
				.map(UserEntity::toDomain);
	}

	@Override
	public Optional<User> findByNickname(Nickname nickname) {
		return userRepo.findByNickname(nickname.getValue())
				.map(UserEntity::toDomain);
	}

	@Override
	public Optional<User> findByUserId(Long userId) {
		return userRepo.findById(userId)
				.map(UserEntity::toDomain);
	}

	@Override
	public boolean passwordMatch(User user) {
		String inputPassword = user.getPassword().getValue();
		String encodedPassword = userRepo.encodedPassword(user.getEmail().getValue());
		
		return passwordEncoder.matches(inputPassword, encodedPassword);
	}
	
}
