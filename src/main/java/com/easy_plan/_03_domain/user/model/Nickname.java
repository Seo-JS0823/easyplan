package com.easy_plan._03_domain.user.model;

import com.easy_plan._03_domain.ValidErrorCode;
import com.easy_plan._03_domain.user.exception.UserException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Nickname {
	private static final int MIN_LENGTH = 2;
	private static final int MAX_LENGTH = 10;
	
	private final String value;
	
	public Nickname(String value) {
		validate(value);
		this.value = value;
	}
	
	private void validate(String value) {
		if(value == null) {
			throw new UserException(new ValidErrorCode("입력해주세요.", this));
		}
		
		int length = value.length();
		
		if(length < MIN_LENGTH || length > MAX_LENGTH) {
			throw new UserException(new ValidErrorCode("2~10자의 닉네임만 사용 가능합니다.", this));
		}
		
		if(value.contains(" ")) {
			throw new UserException(new ValidErrorCode("공백을 허용하지 않습니다.", this));
		}
	}
}
