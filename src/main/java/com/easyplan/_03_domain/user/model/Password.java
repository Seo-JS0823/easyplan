package com.easyplan._03_domain.user.model;

import java.util.regex.Pattern;

import com.easyplan._03_domain.user.exception.UserException;
import com.easyplan._03_domain.user.exception.UserValueError;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Password {
	private static final Pattern PASSWORD = Pattern.compile("^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[a-z\\d!@#$%^&*]{9,}$");
	
	private final String value;
	
	Password(String value) {
		this.value = value;
	}
	
	public static Password of(String value) {
		if(value == null || value.isBlank() || !PASSWORD.matcher(value).matches()) {
			throw new UserException(UserValueError.INVALID_PASSWORD);
		}
		
		return new Password(value);
	}

}
