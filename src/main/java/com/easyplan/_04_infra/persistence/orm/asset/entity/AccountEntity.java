package com.easyplan._04_infra.persistence.orm.asset.entity;

import com.easyplan._03_domain.asset.exception.AssetError;
import com.easyplan._03_domain.asset.exception.AssetException;
import com.easyplan._03_domain.asset.model.account.Account;
import com.easyplan._03_domain.asset.model.account.AccountName;
import com.easyplan._03_domain.asset.model.account.AccountStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ACCOUNT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id", nullable = false, updatable = false)
	private Long userId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private CategoryEntity category;
	
	@Column(name = "name", nullable = false, length = 20)
	private String name;
	
	@Column(name = "status", nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private AccountStatus status;
	
	public static AccountEntity create(Account account, CategoryEntity category) {
		AccountEntity entity = new AccountEntity();
		entity.id = account.getId();
		entity.userId = account.getUserId();
		entity.category = category;
		entity.name = account.getName().getValue();
		entity.status = account.getStatus();
		return entity;
	}
	
	public Account toDomain() {
		return Account.read(id, userId, category.toDomain().getId(), AccountName.systemOf(name), status);
	}
	
	public void changeCategory(CategoryEntity category) {
		updateStatusGuard();
		this.category = category;
	}
	
	public void changeAccountName(AccountName name) {
		updateStatusGuard();
		this.name = name.getValue();
	}
	
	public void updateStatusGuard() {
		if(this.status == AccountStatus.DISABLED) {
			throw new AssetException(AssetError.UPDATE_ACCOUNT_DISABLED);
		}
	}
	
	public void disable() {
		if(this.status == AccountStatus.DISABLED) {
			return;
		}
		
		this.status = AccountStatus.DISABLED;
	}
}
