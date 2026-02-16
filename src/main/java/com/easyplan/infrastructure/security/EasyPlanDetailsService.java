package com.easyplan.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.easyplan.common.error.GlobalException;
import com.easyplan.domain.user.model.PublicId;
import com.easyplan.domain.user.model.User;
import com.easyplan.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EasyPlanDetailsService implements UserDetailsService {

	private final UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		PublicId publicId = PublicId.of(username);
		
		try {
			User user = userRepo.findByPublicId(publicId);
			
			return new EasyPlanUserDetails(user);
		} catch (GlobalException e) {
			throw new UsernameNotFoundException(e.getErrorCode().getMessage());
		}
	}

}
