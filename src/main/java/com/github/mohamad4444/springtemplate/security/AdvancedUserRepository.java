package com.github.mohamad4444.springtemplate.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import com.github.mohamad4444.springtemplate.model.AdvancedUserEntity;

public interface AdvancedUserRepository extends JpaRepository<AdvancedUserEntity, UUID>{
	Optional<AdvancedUserEntity> findByEmail(String email);
}
