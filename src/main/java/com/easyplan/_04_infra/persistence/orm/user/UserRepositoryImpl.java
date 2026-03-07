package com.easyplan._04_infra.persistence.orm.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.easyplan._03_domain.user.model.Email;
import com.easyplan._03_domain.user.model.Nickname;
import com.easyplan._03_domain.user.model.PublicId;
import com.easyplan._03_domain.user.model.User;
import com.easyplan._03_domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final JpaUserRepository userJpa;

	@Override
	public boolean existsByEmail(Email email) {
		return userJpa.existsByEmail(email.getValue());
	}

	@Override
	public boolean existsByNickname(Nickname nickname) {
		return userJpa.existsByNickname(nickname.getValue());
	}

	@Override
	public Optional<User> findById(Long id) {
		return userJpa.findById(id)
				.map(UserEntity::toDomain);
	}

	@Override
	public Optional<User> findByEmail(Email email) {
		return userJpa.findByEmail(email.getValue())
				.map(UserEntity::toDomain);
	}

	@Override
	public Optional<User> findByPublicId(PublicId publicId) {
		return userJpa.findByPublicId(publicId.getValue())
				.map(UserEntity::toDomain);
	}

	@Override
	public User save(User user) {
		UserEntity entity = UserEntity.create(user);
		UserEntity saved = userJpa.save(entity);
		
		return saved.toDomain();
	}

	@Override
	public void updateNickname(User updateUser) {
		UserEntity current = getForUpdate(updateUser);
		
		current.changeNickname(updateUser.getNickname(), updateUser.getUpdatedAt());
	}

	@Override
	public void updatePasswordHash(User updateUser) {
		UserEntity current = getForUpdate(updateUser);
		
		current.changePasswordHash(updateUser.getPasswordHash(), updateUser.getUpdatedAt());
	}
	
	private UserEntity getForUpdate(User user) {
		return userJpa.findById(user.getId())
				.orElseThrow(() -> new UserDataAccessException(UserDataAccessError.UPDATE_USER_NOT_FOUND));
	}
	
}
