package com.easyplan.infrastructure.persistence.user.repository;

import org.springframework.stereotype.Repository;

import com.easyplan.domain.user.error.UserErrorCode;
import com.easyplan.domain.user.error.UserException;
import com.easyplan.domain.user.model.Email;
import com.easyplan.domain.user.model.Nickname;
import com.easyplan.domain.user.model.PasswordHash;
import com.easyplan.domain.user.model.PublicId;
import com.easyplan.domain.user.model.User;
import com.easyplan.domain.user.repository.UserRepository;
import com.easyplan.infrastructure.persistence.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final JpaUserRepository userRepo;
	
	@Override
	public User save(User user) {
		String pid = user.publicId().getValue();
		
		UserEntity entity = userRepo.findByPublicId(pid)
				.map(existing -> {
					existing.apply(user);
					return existing;
				})
				.orElseGet(() -> UserEntity.create(user));
		
		UserEntity saved = userRepo.save(entity);
		
		return saved.toDomain();
	}

	@Override
	public boolean existsByEmail(Email email) {
		String inputEmail = email.getValue();
		
		return userRepo.existsByEmail(inputEmail);
	}

	@Override
	public boolean existsByNickname(Nickname nickname) {
		String inputNickname = nickname.getValue();
		
		return userRepo.existsByNickname(inputNickname);
	}

	@Override
	public User findByEmail(Email email) {
		UserEntity entity = userRepo.findByEmail(email.getValue())
				.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
		
		return entity.toDomain();
	}

	@Override
	public User findByPublicId(PublicId publicId) {
		UserEntity entity = userRepo.findByPublicId(publicId.getValue())
				.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
		
		return entity.toDomain();
	}

	@Override
	public PasswordHash getPasswordHash(PublicId publicId) {
		String passwordHash = userRepo.getPasswordHash(publicId.getValue());
		
		if(passwordHash == null) {
			throw new UserException(UserErrorCode.USER_NOT_FOUND);
		}
		
		return PasswordHash.of(passwordHash);
	}
}