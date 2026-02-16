package com.easyplan._02_application;

import org.springframework.security.core.context.SecurityContextHolder;

import com.easyplan.domain.auth.model.Subject;
import com.easyplan.domain.user.model.PublicId;

public class PublicIdHolder {
	
	public static PublicId getPublicId() {
		String publicId = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return PublicId.of(publicId);
	}
	
	public static Subject getSubject() {
		return Subject.of(getPublicId());
	}
}
