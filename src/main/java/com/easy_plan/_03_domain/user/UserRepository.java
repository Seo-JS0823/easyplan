package com.easy_plan._03_domain.user;

import java.util.Optional;

import com.easy_plan._03_domain.user.model.Email;
import com.easy_plan._03_domain.user.model.Nickname;
import com.easy_plan._03_domain.user.model.User;

public interface UserRepository {

	boolean existsByEmail(Email email);
	
	boolean existsByNickname(Nickname nickname);
	
	User save(User user);
	
	Optional<User> findByEmail(Email email);
	
	Optional<User> findByNickname(Nickname nickname);
	
	Optional<User> findByUserId(Long userId);
	
	boolean passwordMatch(User user);
}
