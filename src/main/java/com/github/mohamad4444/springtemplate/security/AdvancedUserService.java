package com.github.mohamad4444.springtemplate.security;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import com.github.mohamad4444.springtemplate.model.AdvancedUserEntity;

@Service
public class AdvancedUserService {
	private AdvancedUserRepository userRepository;

	public AdvancedUserService(AdvancedUserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public Optional<AdvancedUserEntity>findById(UUID id) {
		return this.userRepository.findById(id);
	}
	
	public Optional<AdvancedUserEntity>findByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}
	
	public AdvancedUserEntity save(AdvancedUserEntity userEntity) {
		return this.userRepository.save(userEntity);
	}
}
