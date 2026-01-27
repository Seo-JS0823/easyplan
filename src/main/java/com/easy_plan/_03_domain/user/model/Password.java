package com.easy_plan._03_domain.user.model;

import java.util.regex.Pattern;

import com.easy_plan._03_domain.ValidErrorCode;
import com.easy_plan._03_domain.user.exception.UserException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Password {
	private static final String REGEX = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{9,}$";
	
	private static final Pattern PATTERN = Pattern.compile(REGEX);
	
	private final String value;
	
	public Password(String value) {
		validate(value);
		this.value = value;
	}
	
	private void validate(String value) {
		if(value == null) {
			throw new UserException(new ValidErrorCode("필수 값 입니다.", this));
		}
		
		if(!PATTERN.matcher(value).matches()) {
			throw new UserException(new ValidErrorCode("영문 소문자, 숫자, 특수문자를 포함한 9자 이상으로 입력해주세요.", this));
		}
	}
}
