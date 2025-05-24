package com.github.mohamad4444.springtemplate.examples.data2Security2.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID>{
	Optional<UserEntity> findByEmail(String email);
}
