package com.easyplan._03_domain.asset.model.account.category;

import com.easyplan._03_domain.asset.exception.AssetException;
import com.easyplan._03_domain.asset.exception.AssetValueError;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CategoryName {
	private final String value;
	
	private static final int ACCOUNT_NAME_MAX_LENGTH = 10;
	
	private CategoryName(String value) {
		this.value = value;
	}
	
	public static CategoryName of(String value) {
		if(value == null || value.isBlank()) {
			throw new AssetException(AssetValueError.NOT_FOUND_CATEGORY_NAME);
		}
		
		if(value.length() > ACCOUNT_NAME_MAX_LENGTH) {
			throw new AssetException(AssetValueError.INVALID_CATEGORY_NAME);
		}
		
		return new CategoryName(value);
	}
}
