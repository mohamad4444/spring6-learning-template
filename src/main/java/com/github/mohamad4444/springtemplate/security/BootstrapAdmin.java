package com.github.mohamad4444.springtemplate.security;

import com.github.mohamad4444.springtemplate.model.AdvancedUserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Component
@ConditionalOnProperty(name = "app.bootstrap.enabled", havingValue = "true")
public class BootstrapAdmin implements CommandLineRunner {
    private final AdvancedUserRepository users;
    private final PasswordEncoder encoder;
    private final String email;
    private final String password;
    public BootstrapAdmin(AdvancedUserRepository users, PasswordEncoder encoder,
            @Value("${BOOTSTRAP_ADMIN_EMAIL}") String email,
            @Value("${BOOTSTRAP_ADMIN_PASSWORD}") String password) {
        if (email.isBlank() || password.length() < 16) {
            throw new IllegalArgumentException("Bootstrap requires an email and a password of at least 16 characters");
        }
        this.users = users; this.encoder = encoder; this.email = email; this.password = password;
    }
    @Override
    @Transactional
    public void run(String... args) {
        if (users.findByEmail(email).isEmpty()) {
            users.save(new AdvancedUserEntity("Template", "Administrator", email,
                    encoder.encode(password), Set.of("ADMIN", "WRITER")));
        }
    }
}
