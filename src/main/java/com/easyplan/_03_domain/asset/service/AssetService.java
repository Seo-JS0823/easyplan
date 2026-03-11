package com.easyplan._03_domain.asset.service;

import java.util.List;

import com.easyplan._03_domain.asset.exception.AssetError;
import com.easyplan._03_domain.asset.exception.AssetException;
import com.easyplan._03_domain.asset.model.account.Account;
import com.easyplan._03_domain.asset.model.account.AccountSystemList;
import com.easyplan._03_domain.asset.model.account.category.Category;
import com.easyplan._03_domain.asset.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssetService {
	private final AccountRepository accountRepo;
	
	public void createSystemAccounts(Long userId) {
		if(accountRepo.existsOriginCategory(userId)) {
			throw new AssetException(AssetError.ALREADY_EXISTS_SYSTEM_CATEGORY);
		}
		
		List<Category> systemCategories = accountRepo.createSystemCategory(userId);
		
		List<Account> systemAccount = AccountSystemList.getSystemAccountNames(userId, systemCategories);
		
		List<Account> systemAccountSaved = accountRepo.createSystemAccount(systemAccount);
	}
}
