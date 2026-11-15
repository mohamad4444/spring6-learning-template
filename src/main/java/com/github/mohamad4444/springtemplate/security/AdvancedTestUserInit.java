package com.github.mohamad4444.springtemplate.security;

import com.github.mohamad4444.springtemplate.model.AdvancedUserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Component
@Profile("dev | academic")
@ConditionalOnProperty(name = "app.demo-users.enabled", havingValue = "true")
public class AdvancedTestUserInit implements CommandLineRunner {
    private final AdvancedUserRepository users;
    private final PasswordEncoder encoder;
    public AdvancedTestUserInit(AdvancedUserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }
    @Override
    @Transactional
    public void run(String... args) {
        create("Armin", "Nistrator", "armin.nistrator@hs-kl.de", "admin-secret", Set.of("ADMIN", "WRITER"));
        create("Ester", "Nutzer", "ester.nutzer@hs-kl.de", "user1-secret", Set.of("WRITER"));
        create("Anders", "Nutzer", "anders.nutzer@hs-kl.de", "user2-secret", Set.of("WRITER"));
    }
    private void create(String first, String last, String email, String password, Set<String> roles) {
        if (users.findByEmail(email).isEmpty()) {
            users.save(new AdvancedUserEntity(first, last, email, encoder.encode(password), roles));
        }
    }
}
