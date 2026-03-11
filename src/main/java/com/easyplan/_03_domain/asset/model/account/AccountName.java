package com.easyplan._03_domain.asset.model.account;

import com.easyplan._03_domain.asset.exception.AssetException;
import com.easyplan._03_domain.asset.exception.AssetValueError;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class AccountName {
	private final String value;
	
	private static final int ACCOUNT_NAME_MAX_LENGTH = 20;
	
	private AccountName(String value) {
		this.value = value;
	}
	
	public static AccountName of(String value) {
		if(value == null || value.isBlank()) {
			throw new AssetException(AssetValueError.NOT_FOUND_ACCOUNT_NAME);
		}
		
		if(value.length() > ACCOUNT_NAME_MAX_LENGTH) {
			throw new AssetException(AssetValueError.INVALID_ACCOUNT_NAME);
		}
		
		return new AccountName(value);
	}
	
	public static AccountName systemOf(String value) {
		return new AccountName(value);
	}
}
