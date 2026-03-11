package com.easyplan.asset.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.easyplan._03_domain.asset.exception.AssetException;
import com.easyplan._03_domain.asset.model.account.Account;
import com.easyplan._03_domain.asset.model.account.AccountName;
import com.easyplan._03_domain.asset.model.account.AccountStatus;
import com.easyplan._03_domain.asset.model.account.AccountSystemList;
import com.easyplan._03_domain.asset.model.account.category.Category;
import com.easyplan._03_domain.asset.model.account.category.CategoryName;
import com.easyplan._03_domain.asset.model.account.category.CategoryType;
import com.easyplan._03_domain.asset.model.account.category.OriginType;

public class AccountSystemListTest {
	@Test
	void getSystemAccountNames() {
		Long userId = 1L;
		
		List<Category> systemCategories = Arrays.asList(
        new Category(101L, userId, CategoryName.of("자산"), CategoryType.ASSET, OriginType.SYSTEM),
        new Category(102L, userId, CategoryName.of("부채"), CategoryType.LIABILITY, OriginType.SYSTEM),
        new Category(103L, userId, CategoryName.of("자본"), CategoryType.EQUITY, OriginType.SYSTEM),
        new Category(104L, userId, CategoryName.of("수익"), CategoryType.INCOME, OriginType.SYSTEM),
        new Category(105L, userId, CategoryName.of("비용"), CategoryType.EXPENSE, OriginType.SYSTEM)
    );
		
		List<Account> result = AccountSystemList.getSystemAccountNames(userId, systemCategories);
		
		boolean allActive = result.stream()
        .allMatch(account -> account.getStatus() == AccountStatus.ACTIVE);
    
    assertTrue(allActive, "초기 생성된 모든 계정은 ACTIVE 상태여야 합니다.");
    
    boolean allIdsNull = result.stream()
        .allMatch(account -> account.getId() == null);
    
    assertTrue(allIdsNull, "새로 생성된 계정의 ID는 null이어야 합니다.");
    
    Account salaryAccount = result.stream()
        .filter(a -> a.getName().equals(AccountName.of("월급")))
        .findFirst()
        .orElseThrow();
    
    assertEquals(104L, salaryAccount.getCategoryId(), "월급은 INCOME 타입 카테고리(104)에 속해야 함");
    assertEquals(userId, salaryAccount.getUserId(), "요청한 유저 ID와 일치해야 함");
    
    System.out.println(result);
	}
	
	@Test
  @DisplayName("카테고리 누락 시 예외 발생 테스트")
  void missingCategoryTest() {
      // Given: ASSET 타입 카테고리가 없는 빈 리스트
      Long userId = 1L;
      List<Category> emptyCategories = List.of();

      // When & Then: 예외가 발생하는지 확인
      assertThrows(AssetException.class, () -> {
          AccountSystemList.getSystemAccountNames(userId, emptyCategories);
      });
  }
}
