package com.github.mohamad4444.springtemplate.config;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

@Component
public class ProfileGuard {
    public ProfileGuard(Environment environment) {
        if (environment.acceptsProfiles(Profiles.of("prod")) &&
                environment.acceptsProfiles(Profiles.of("dev", "academic", "test"))) {
            throw new IllegalStateException("prod cannot be combined with dev, academic or test");
        }
        if (environment.acceptsProfiles(Profiles.of("mysql")) && environment.acceptsProfiles(Profiles.of("postgresql"))) {
            throw new IllegalStateException("Choose either mysql or postgresql");
        }
        if (environment.acceptsProfiles(Profiles.of("jwt")) && environment.acceptsProfiles(Profiles.of("academic"))) {
            throw new IllegalStateException("academic examples require local account authentication");
        }
    }
}
