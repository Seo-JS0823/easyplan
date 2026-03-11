package com.easyplan._04_infra.persistence.orm.asset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.easyplan._03_domain.asset.exception.AssetError;
import com.easyplan._03_domain.asset.exception.AssetException;
import com.easyplan._03_domain.asset.model.account.Account;
import com.easyplan._03_domain.asset.model.account.category.Category;
import com.easyplan._03_domain.asset.model.account.category.CategorySystems;
import com.easyplan._03_domain.asset.model.account.category.OriginType;
import com.easyplan._03_domain.asset.repository.AccountRepository;
import com.easyplan._04_infra.persistence.orm.asset.entity.AccountEntity;
import com.easyplan._04_infra.persistence.orm.asset.entity.CategoryEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
	private final JpaAccountRepository accountJpa;
	
	private final JpaCategoryRepository categoryJpa;
	
	@Override
	public boolean existsOriginCategory(Long userId) {
		return categoryJpa.existsByUserIdAndOrigin(userId, OriginType.SYSTEM);
	}
	
	@Override
	public List<Category> createSystemCategory(Long userId) {
		List<Category> createSystemCategories = CategorySystems.getSystemCategories(userId);
		
		List<CategoryEntity> entities = createSystemCategories.stream()
				.map(CategoryEntity::create)
				.toList();
		
		List<CategoryEntity> saves = categoryJpa.saveAll(entities);
		
		return saves.stream()
				.map(CategoryEntity::toDomain)
				.toList();
	}
	
	@Override
	public List<Account> createSystemAccount(List<Account> accountList) {
		List<AccountEntity> entities = accountList.stream()
				.map(account -> {
					CategoryEntity categoryEntity = categoryJpa.findById(account.getCategoryId())
							.orElseThrow(() -> new AssetException(AssetError.UPDATE_SYSTEM_CATEGORY));
					
					return AccountEntity.create(account, categoryEntity);
				})
				.toList();
		
		List<AccountEntity> saves = accountJpa.saveAll(entities);
		
		return saves.stream()
				.map(AccountEntity::toDomain)
				.toList();
	}

	@Override
	public List<Category> getSystemCategoryList(Long userId) {
		return categoryJpa.findByUserIdAndOrigin(userId, OriginType.SYSTEM).stream()
				.map(CategoryEntity::toDomain)
				.toList();
	}
	
	@Override
	public Category createCategory(Category category) {
		CategoryEntity entity = CategoryEntity.create(category);
		CategoryEntity saved = categoryJpa.save(entity);
		return saved.toDomain();
	}

	@Override
	public Optional<Category> getCategory(Long userId, Long id) {
		return categoryJpa.findByIdAndUserId(id, userId)
				.map(CategoryEntity::toDomain);
	}

	@Override
	public List<Category> getCategoryList(Long userId) {
		return categoryJpa.findByUserId(userId).stream()
				.map(CategoryEntity::toDomain)
				.toList();
	}
	
	@Override
	public Account createAccount(Account account) {
		CategoryEntity proxyCategory = categoryJpa.getReferenceById(account.getCategoryId());
		
		AccountEntity entity = AccountEntity.create(account, proxyCategory);
		AccountEntity saved = accountJpa.save(entity);
		
		return saved.toDomain();
	}
}
