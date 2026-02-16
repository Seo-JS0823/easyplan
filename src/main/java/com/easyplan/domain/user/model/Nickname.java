package com.easyplan.domain.user.model;

import java.util.regex.Pattern;

import com.easyplan.domain.user.error.UserErrorCode;
import com.easyplan.domain.user.error.UserException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Nickname {
	private static final Pattern NICKNAME = Pattern.compile("^[가-힣a-zA-Z0-9]{2,10}$");
	
	private final String value;
	
	private Nickname(String value) {
		this.value = value;
	}
	
	public static Nickname of(String value) {
		if(value == null || value.isBlank()) {
			throw new UserException(UserErrorCode.INVALID_NICKNAME);
		}
		
		if(!NICKNAME.matcher(value).matches()) {			
			throw new UserException(UserErrorCode.INVALID_NICKNAME);
		}
		
		return new Nickname(value);
	}
	
}
