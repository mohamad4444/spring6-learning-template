package com.github.mohamad4444.springtemplate.security;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import com.github.mohamad4444.springtemplate.model.AdvancedUserEntity;

// only for testing, do not use in production
@Component
public class AdvancedTestUserInit implements CommandLineRunner {
	private final Logger LOGGER = LoggerFactory.getLogger(AdvancedTestUserInit.class);
	
	private static final String ADMIN_ROLE = "ADMIN";
	private static final String WRITER_ROLE = "WRITER";

	private AdvancedUserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public AdvancedTestUserInit(AdvancedUserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {
		this.createUsers();
	}

	@Transactional
	private void createUsers() {
		LOGGER.info("Creating test users...");
		
		final var users = List.of(
				new AdvancedUserEntity(
						"Armin",
						"Nistrator",
						"armin.nistrator@hs-kl.de",
				        this.passwordEncoder.encode("admin-secret"),
				        Set.of(ADMIN_ROLE)
				        ),
				new AdvancedUserEntity(
						"Ester",
						"Nutzer",
						"ester.nutzer@hs-kl.de",
				        this.passwordEncoder.encode("user1-secret"),
				        Set.of(WRITER_ROLE)
				        ),
				new AdvancedUserEntity(
						"Anders",
						"Nutzer",
						"anders.nutzer@hs-kl.de",
				        this.passwordEncoder.encode("user2-secret"),
				        Set.of(WRITER_ROLE)
				        )
				);

		this.userRepository.saveAll(users);
	}

}
