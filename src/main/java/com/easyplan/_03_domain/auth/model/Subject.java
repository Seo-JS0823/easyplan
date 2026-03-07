package com.easyplan._03_domain.auth.model;

import com.easyplan._03_domain.auth.exception.AuthException;
import com.easyplan._03_domain.auth.exception.AuthValueError;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Subject {
	private final String value;
	
	private Subject(String value) {
		this.value = value;
	}
	
	public static Subject of(String value) {
		if(value == null || value.isBlank()) {
			throw new AuthException(AuthValueError.INVALID_SUBJECT);
		}
		
		return new Subject(value);
	}
}
