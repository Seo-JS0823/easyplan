package com.easyplan._04_infra.persistence.orm.asset.entity;

import com.easyplan._03_domain.asset.model.account.category.Category;
import com.easyplan._03_domain.asset.model.account.category.CategoryDirection;
import com.easyplan._03_domain.asset.model.account.category.CategoryName;
import com.easyplan._03_domain.asset.model.account.category.CategoryType;
import com.easyplan._03_domain.asset.model.account.category.OriginType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CATEGORY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id", nullable = false)
	private Long userId;
	
	@Column(name = "name", nullable = false, length = 10)
	private String name;
	
	@Column(name = "type", nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private CategoryType type;
	
	@Column(name = "direction", nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private CategoryDirection direction;
	
	@Column(name = "origin", nullable = false, updatable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private OriginType origin;
	
	public static CategoryEntity create(Category category) {
		CategoryEntity entity = new CategoryEntity();
		entity.id = category.getId();
		entity.userId = category.getUserId();
		entity.name = category.getName().getValue();
		entity.type = category.getType();
		entity.direction = category.getDirection();
		entity.origin = category.getOrigin();
		return entity;
	}
	
	public Category toDomain() {
		return Category.read(id, userId, CategoryName.of(name), type, origin);
	}
}
