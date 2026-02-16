package com.easyplan.domain.user.model;

import java.util.regex.Pattern;

import com.easyplan.domain.user.error.UserErrorCode;
import com.easyplan.domain.user.error.UserException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Password {
	private static final Pattern PASSWORD = Pattern.compile("^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[a-z\\d!@#$%^&*]{9,}$");
	
	private final String value;
	
	public Password(String value) {
		this.value = value;
	}
	
	public static Password of(String value) {
		if(value == null || value.isBlank() || !PASSWORD.matcher(value).matches()) {
			throw new UserException(UserErrorCode.INVALID_PASSWORD);
		}
		
		return new Password(value);
	}
}
