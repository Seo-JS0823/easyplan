package com.easy_plan._03_domain.user;

import com.easy_plan._03_domain.user.model.Email;
import com.easy_plan._03_domain.user.model.Nickname;
import com.easy_plan._03_domain.user.model.User;

public interface UserRepository {

	boolean existsByEmail(Email email);
	
	boolean existsByNickname(Nickname nickname);
	
	User save(User user);
}
