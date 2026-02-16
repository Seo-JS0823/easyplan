package com.easyplan.domain.auth.model;

import java.util.Objects;

import com.easyplan.domain.user.model.PublicId;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Subject {
	private final PublicId publicId;
	
	private Subject(PublicId publicId) {
		this.publicId = Objects.requireNonNull(publicId, "User PublicId must not be null");
	}
	
	public static Subject of(PublicId publicId) {
		return new Subject(publicId);
	}
	
	public PublicId userPublicId() {
		return publicId;
	}
}
