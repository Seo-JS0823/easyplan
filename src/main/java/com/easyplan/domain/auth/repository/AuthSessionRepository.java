package com.easyplan.domain.auth.repository;

import com.easyplan.domain.auth.model.AuthSession;
import com.easyplan.domain.auth.model.Subject;

public interface AuthSessionRepository {
	AuthSession save(AuthSession authSession);
	
	AuthSession findBySubject(Subject subject);
}
