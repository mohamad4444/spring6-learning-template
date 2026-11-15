package com.github.mohamad4444.springtemplate;

import com.github.mohamad4444.springtemplate.config.ProfileGuard;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import static org.assertj.core.api.Assertions.*;

class ProfileGuardTests {
    @Test void rejectsUnsafeProductionCombinations() {
        var environment = new MockEnvironment(); environment.setActiveProfiles("prod", "academic");
        assertThatThrownBy(() -> new ProfileGuard(environment)).isInstanceOf(IllegalStateException.class);
    }
    @Test void rejectsCompetingDatabaseProfiles() {
        var environment = new MockEnvironment(); environment.setActiveProfiles("mysql", "postgresql");
        assertThatThrownBy(() -> new ProfileGuard(environment)).isInstanceOf(IllegalStateException.class);
    }
    @Test void acceptsProductionWithExternalIntegrations() {
        var environment = new MockEnvironment(); environment.setActiveProfiles("prod", "mongo", "messaging", "jwt");
        assertThatCode(() -> new ProfileGuard(environment)).doesNotThrowAnyException();
    }
}
