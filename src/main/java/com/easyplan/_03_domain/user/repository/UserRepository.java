package com.easyplan._03_domain.user.repository;

import java.util.Optional;

import com.easyplan._03_domain.user.model.Email;
import com.easyplan._03_domain.user.model.Nickname;
import com.easyplan._03_domain.user.model.PublicId;
import com.easyplan._03_domain.user.model.User;

public interface UserRepository {
	boolean existsByEmail(Email email);
	
	boolean existsByNickname(Nickname nickname);
	
	Optional<User> findById(Long id);
	
	Optional<User> findByEmail(Email email);
	
	Optional<User> findByPublicId(PublicId publicId);
	
	User save(User user);
	
	void updateNickname(User updateUser);
	
	void updatePasswordHash(User updateUser);
}