package com.easyplan._03_domain.user.model;

import java.util.UUID;

import com.easyplan._03_domain.user.exception.UserException;
import com.easyplan._03_domain.user.exception.UserValueError;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PublicId {
	private final String value;
	
	PublicId(String value) {
		this.value = value;
	}
	
	public static PublicId of(String value) {
		if(value == null || value.isBlank()) {
			throw new UserException(UserValueError.INVALID_PUBLIC_ID);
		}
		
		try {
			UUID.fromString(value);
		} catch (IllegalArgumentException e) {
			throw new UserException(UserValueError.INVALID_PUBLIC_ID, e);
		}
		
		return new PublicId(value);
	}
	
	public static PublicId create() {
		return new PublicId(UUID.randomUUID().toString());
	}
}
