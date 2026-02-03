package com.easy_plan._04_infra.user;

import java.time.Instant;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
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
		boolean exists = userRepo.existsByEmail(email.getValue());
		if(exists) {
			throw new UserException(UserErrorCode.ALREADY_EMAIL);
		}
		
		return true;
	}

	@Override
	public boolean existsByNickname(Nickname nickname) {
		boolean exists = userRepo.existsByNickname(nickname.getValue());
		if(exists) {
			throw new UserException(UserErrorCode.ALREADY_NICKNAME);
		}
		
		return true;
	}

	@Override
	public User save(User user) {
		existsByEmail(user.getEmail());
		existsByNickname(user.getNickname());
		
		Instant now = clock.now();
		user.encodePassword(passwordEncoder.encode(user.getPassword().getValue()));
		user.createdAt(now);
		user.updatedAt(now);
		UserEntity entity = UserEntity.toEntity(user);
		UserEntity saved;
		
		try {
			saved = userRepo.save(entity);
		} catch (Exception e) {
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
	@Nullable
	public User passwordMatch(User user) {
		UserEntity entity = userRepo.findByEmail(user.getEmail().getValue())
				.orElseThrow(() -> new UserException(UserErrorCode.LOGIN_NOT_MATCH));
		
		String inputPassword = user.getPassword().getValue();
		String encodedPassword = entity.getPassword();
		
		if(passwordEncoder.matches(inputPassword, encodedPassword)) {
			return entity.toDomain();
		}
		return null;
	}
	
}
