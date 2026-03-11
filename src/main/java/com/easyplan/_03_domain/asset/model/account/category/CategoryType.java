package com.easyplan._03_domain.asset.model.account.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
	ASSET(CategoryDirection.DEBIT),			// 자산
	LIABILITY(CategoryDirection.CREDIT),	// 부채
	EQUITY(CategoryDirection.CREDIT),		// 기초 잔액
	INCOME(CategoryDirection.CREDIT),		// 수입
	EXPENSE(CategoryDirection.DEBIT)			// 비용
	;
	
	private final CategoryDirection direction;
}
