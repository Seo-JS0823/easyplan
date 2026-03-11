package com.easyplan._04_infra.persistence.orm.asset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyplan._03_domain.asset.model.account.category.OriginType;
import com.easyplan._04_infra.persistence.orm.asset.entity.CategoryEntity;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {
	boolean existsByUserIdAndOrigin(Long userId, OriginType origin);
	
	Optional<CategoryEntity> findByIdAndUserId(Long id, Long userId);
	
	List<CategoryEntity> findByUserId(Long userId);
	
	List<CategoryEntity> findByUserIdAndOrigin(Long userId, OriginType origin);
}
