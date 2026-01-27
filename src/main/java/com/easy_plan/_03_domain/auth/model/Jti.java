package com.easy_plan._03_domain.auth.model;

import java.util.UUID;

import com.easy_plan._03_domain.ValidErrorCode;
import com.easy_plan._03_domain.ValidException;
import com.easy_plan._03_domain.user.model.Email;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@EqualsAndHashCode
@Slf4j
public class Jti {
	private final String value;
	
	public Jti(String value) {
		validate(value);
		this.value = value;
	}
	
	private void validate(String value) {
		if(value == null) {
			throw new ValidException(new ValidErrorCode("인증 정보가 존재하지 않습니다.", "Security: [JTI]"));
		}
		
		try {
			UUID.fromString(value);
		} catch (IllegalArgumentException e) {
			throw new ValidException(new ValidErrorCode("인증 형식이 잘못되었습니다.", "Security: [JTI]"));
		}
	}
	
	public static void main(String[] args) {
		Email e = new Email(null);
	}
}
