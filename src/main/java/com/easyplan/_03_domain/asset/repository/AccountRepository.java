package com.easyplan._03_domain.asset.repository;

import java.util.List;
import java.util.Optional;

import com.easyplan._03_domain.asset.model.account.Account;
import com.easyplan._03_domain.asset.model.account.category.Category;

public interface AccountRepository {

	/*
	 * 제공 기능 사항
	 * 
	 * Category 생성 param: Category
	 * Category 조회 param: userId, id
	 * Categories 조회 param: userId
	 * 시스템 Category 존재 유무 확인 param: userId
	 * 시스템 Categories 생성 param: userId
	 * 시스템 Categories 조회 param: userId
	 * 시스템 AccountList 생성 param: List<Account>
	 * 
	 * Account 생성
	 * Account 조회
	 * Category 에 속한 AccountList 조회
	 */
	
	public Category createCategory(Category category);
	
	public boolean existsOriginCategory(Long userId);
	
	public List<Category> createSystemCategory(Long userId);
	
	public Optional<Category> getCategory(Long userId, Long id);
	
	public List<Category> getCategoryList(Long userId);
	
	public List<Category> getSystemCategoryList(Long userId);
	
	public List<Account> createSystemAccount(List<Account> accountList);
	
	public Account createAccount(Account account);
}
