package com.easyplan.domain.user.model;

import java.util.UUID;
import java.util.regex.Pattern;

import com.easyplan.domain.user.error.UserErrorCode;
import com.easyplan.domain.user.error.UserException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PublicId {
	private static final Pattern UUID_PATTERN =
			Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");
	
	private final String value;
	
	private PublicId(String value) {
		this.value = value;
	}
	
	public static PublicId of(String value) {
		if(value == null || value.isBlank()) {
			throw new UserException(UserErrorCode.INVALID_PUBLIC_ID);
		}
		
		if(!UUID_PATTERN.matcher(value).matches()) {
			throw new UserException(UserErrorCode.INVALID_PUBLIC_ID);
		}
		
		return new PublicId(value);
	}
	
	public static PublicId create() {
		return new PublicId(UUID.randomUUID().toString());
	}
}
