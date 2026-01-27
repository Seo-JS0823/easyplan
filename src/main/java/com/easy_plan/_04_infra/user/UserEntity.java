package com.easy_plan._04_infra.user;

import com.easy_plan._03_domain.user.model.User;

import jakarta.persistence.Entity;

@Entity
public class UserEntity {

	public static UserEntity toEntity(User user) {
		return null;
	}
	
	public User toDomain() {
		return null;
	}
}
