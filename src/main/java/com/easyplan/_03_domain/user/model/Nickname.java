package com.easyplan._03_domain.user.model;

import java.util.regex.Pattern;

import com.easyplan._03_domain.user.exception.UserException;
import com.easyplan._03_domain.user.exception.UserValueError;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Nickname {
	private static final Pattern NICKNAME = Pattern.compile("^[가-힣a-zA-Z0-9]{2,10}$");
	
	private final String value;
	
	Nickname(String value) {
		this.value = value;
	}
	
	public static Nickname of(String value) {
		if(value == null || value.isBlank() || !NICKNAME.matcher(value).matches()) {
			throw new UserException(UserValueError.INVALID_NICKNAME);
		}
		
		return new Nickname(value);
	}
}
