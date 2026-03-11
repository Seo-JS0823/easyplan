package com.easyplan._03_domain.asset.model.account.category;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategorySystems {

	ASSET(CategoryName.of("자산"), CategoryType.ASSET),
	LIABILITY(CategoryName.of("부채"), CategoryType.LIABILITY),
	EQUITY(CategoryName.of("순자산"), CategoryType.EQUITY),
	INCOME(CategoryName.of("수입"), CategoryType.INCOME),
	EXPENSE(CategoryName.of("비용"), CategoryType.EXPENSE)
	;
	
	private final CategoryName name;
	
	private final CategoryType type;
	
	public static List<Category> getSystemCategories(Long userId) {
		return Arrays.stream(CategorySystems.values())
				.map(list -> Category.create(userId, list.name, list.type, OriginType.SYSTEM))
				.toList();
	}
	
}
