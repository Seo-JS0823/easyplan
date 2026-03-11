package com.easyplan._03_domain.asset.model.account;

import com.easyplan._03_domain.asset.exception.AssetError;
import com.easyplan._03_domain.asset.exception.AssetException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Account {
	private final Long id;
	
	private final Long userId;
	
	private Long categoryId;
	
	private AccountName name;
	
	private AccountStatus status;
	
	Account(Long id, Long userId, Long categoryId, AccountName name, AccountStatus status) {
		this.id = id;
		this.userId = userId;
		this.categoryId = categoryId;
		this.name = name;
		this.status = status;
	}
	
	public static Account create(Long userId, Long categoryId, AccountName name) {
		return new Account(null, userId, categoryId, name, AccountStatus.ACTIVE);
	}
	
	public static Account read(Long id, Long userId, Long categoryId, AccountName name, AccountStatus status) {
		return new Account(id, userId, categoryId, name, status);
	}
	
	public void changeCategoryId(Long categoryId) {
		validateActive();
		this.categoryId = categoryId;
	}
	
	public void changeAccountNamae(AccountName name) {
		validateActive();
		this.name = name;
	}
	
	public void disable() {
		if(this.status == AccountStatus.ACTIVE) {
			this.status = AccountStatus.DISABLED;
		}
	}
	
	public void active() {
		if(this.status == AccountStatus.DISABLED) {
			this.status = AccountStatus.ACTIVE;
		}
	}
	
	public void validateActive() {
		if(this.status == AccountStatus.DISABLED) {
			throw new AssetException(AssetError.UPDATE_ACCOUNT_DISABLED);
		}
	}

	@Override
	public String toString() {
		return String.format("\nAccount: { id: %s, userId: %s, categoryId: %s, name: %s, status: %s }",
				id, userId, categoryId, name.getValue(), status.name()
		);
	}
	
	
}
