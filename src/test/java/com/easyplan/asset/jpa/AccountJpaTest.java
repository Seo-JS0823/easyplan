package com.easyplan.asset.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.easyplan._03_domain.asset.service.AssetService;

@SpringBootTest
public class AccountJpaTest {
	
	@Autowired
	AssetService assetTest;
	
	@Test
	void accountSystemCreate() {
		Long userId = 1L;
		
		assetTest.createSystemAccounts(userId);
	}
}
