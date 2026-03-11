package com.easyplan._03_domain.asset.model.account.category;

import com.easyplan._03_domain.asset.exception.AssetError;
import com.easyplan._03_domain.asset.exception.AssetException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Category {
	private final Long id;
	
	private final Long userId;
	
	private CategoryName name;
	
	private CategoryType type;
	
	private final CategoryDirection direction;
	
	private final OriginType origin;
	
	public Category(Long id, Long userId, CategoryName name, CategoryType type, OriginType origin) {
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.type = type;
		this.direction = type.getDirection();
		this.origin = origin;
	}
	
	public static Category create(Long userId, CategoryName name, CategoryType type, OriginType origin) {
		return new Category(null, userId, name, type, origin);
	}
	
	public static Category read(Long id, Long userId, CategoryName name, CategoryType type, OriginType origin) {
		return new Category(id, userId, name, type, origin);	
	}
	
	public void updateCategoryName(CategoryName name) {
		updateOriginGuard();
		this.name = name;
	}
	
	public void updateCategoryType(CategoryType type) {
		updateOriginGuard();
		this.type = type;
	}
	
	public void updateOriginGuard() {
		if(this.origin == OriginType.SYSTEM) {
			throw new AssetException(AssetError.UPDATE_SYSTEM_CATEGORY);
		}
	}
}
