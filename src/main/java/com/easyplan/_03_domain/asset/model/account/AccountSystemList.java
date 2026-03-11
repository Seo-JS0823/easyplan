package com.easyplan._03_domain.asset.model.account;

import java.util.Arrays;
import java.util.List;

import com.easyplan._03_domain.asset.exception.AssetException;
import com.easyplan._03_domain.asset.exception.AssetValueError;
import com.easyplan._03_domain.asset.model.account.category.Category;
import com.easyplan._03_domain.asset.model.account.category.CategoryType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountSystemList {
	A1(AccountName.of("체크카드"), CategoryType.ASSET),
	A2(AccountName.of("현금"), CategoryType.ASSET),
	
	L1(AccountName.of("신용카드"), CategoryType.LIABILITY),
	L2(AccountName.of("갚을 돈"), CategoryType.LIABILITY),
	L3(AccountName.of("대출"), CategoryType.LIABILITY),
	
	EQ1(AccountName.of("초기 잔액 설정"), CategoryType.EQUITY),
	
	I1(AccountName.of("월급"), CategoryType.INCOME),
	I3(AccountName.of("판매 수익"), CategoryType.INCOME),
	I4(AccountName.of("기타 수익"), CategoryType.INCOME),
	
	E1(AccountName.of("식비"), CategoryType.EXPENSE),
	E2(AccountName.of("교통비"), CategoryType.EXPENSE),
	E3(AccountName.of("주거 및 통신"), CategoryType.EXPENSE),
	E4(AccountName.of("생활용품"), CategoryType.EXPENSE),
	E5(AccountName.of("지식 및 문화"), CategoryType.EXPENSE),
	E6(AccountName.of("의류 및 미용"), CategoryType.EXPENSE),
	E7(AccountName.of("의료 및 건강"), CategoryType.EXPENSE),
	E8(AccountName.of("취미"), CategoryType.EXPENSE),
	E9(AccountName.of("세금"), CategoryType.EXPENSE),
	E10(AccountName.of("이자"), CategoryType.EXPENSE),
	E11(AccountName.of("기타 지출"), CategoryType.EXPENSE),
	
	;
	private final AccountName name;
	
	private final CategoryType type;
	
	public static List<Account> getSystemAccountNames(Long userId, List<Category> systemCategories) {
		return Arrays.stream(AccountSystemList.values())
				.map(sys -> {
					Category matched = systemCategories.stream()
							.filter(category -> category.getType().equals(sys.type))
							.findFirst()
							.orElseThrow(() -> new AssetException(AssetValueError.NOT_FOUND_CATEGORY));
					
					return Account.create(userId, matched.getId(), sys.name);
				})
				.toList();
	}
}
