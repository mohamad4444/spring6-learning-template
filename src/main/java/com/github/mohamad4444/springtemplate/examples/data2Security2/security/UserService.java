package com.github.mohamad4444.springtemplate.examples.data2Security2.security;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public Optional<UserEntity>findById(UUID id) {
		return this.userRepository.findById(id);
	}
	
	public Optional<UserEntity>findByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}
	
	public UserEntity save(UserEntity userEntity) {
		return this.userRepository.save(userEntity);
	}
}
