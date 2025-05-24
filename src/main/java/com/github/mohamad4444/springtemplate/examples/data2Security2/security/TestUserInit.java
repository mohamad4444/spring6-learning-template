package com.github.mohamad4444.springtemplate.examples.data2Security2.security;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

// only for testing, do not use in production
@Component
public class TestUserInit implements CommandLineRunner {
	private final Logger LOGGER = LoggerFactory.getLogger(TestUserInit.class);
	
	private static final String ADMIN_ROLE = "ADMIN";
	private static final String WRITER_ROLE = "WRITER";

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public TestUserInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
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
				new UserEntity(
						"Armin",
						"Nistrator",
						"armin.nistrator@hs-kl.de",
				        this.passwordEncoder.encode("admin-secret"),
				        Set.of(ADMIN_ROLE)
				        ),
				new UserEntity(
						"Ester",
						"Nutzer",
						"ester.nutzer@hs-kl.de",
				        this.passwordEncoder.encode("user1-secret"),
				        Set.of(WRITER_ROLE)
				        ),
				new UserEntity(
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
