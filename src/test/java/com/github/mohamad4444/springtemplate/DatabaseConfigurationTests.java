package com.github.mohamad4444.springtemplate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.env.StandardEnvironment;
import static org.assertj.core.api.Assertions.*;

class DatabaseConfigurationTests {
    private final ApplicationContextRunner runner = new ApplicationContextRunner().withInitializer(context -> {
        var sources = context.getEnvironment().getPropertySources();
        sources.remove(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME);
        sources.remove(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME);
        new ConfigDataApplicationContextInitializer().initialize(context);
    });

    @Test void postgresOverridesTheLocalH2Defaults() {
        runner.withPropertyValues("spring.profiles.active=dev,postgresql").run(context ->
            assertThat(context.getEnvironment().getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:postgresql://localhost:5432/springtemplate"));
    }
    @Test void mysqlSelectsItsConnectionAndMigrationDefaults() {
        runner.withPropertyValues("spring.profiles.active=dev,mysql").run(context -> {
            assertThat(context.getEnvironment().getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:mysql://localhost:3306/springtemplate");
            assertThat(context.getEnvironment().getProperty("spring.flyway.locations")).isEqualTo("classpath:db/mysql");
        });
    }
    @Test void productionRequiresCredentialsInEitherProfileOrder() {
        for (String profiles : new String[]{"prod,mysql", "mysql,prod"}) {
            runner.withPropertyValues("spring.profiles.active=" + profiles).run(context -> {
                for (String property : new String[]{"spring.datasource.url", "spring.datasource.username", "spring.datasource.password"}) {
                    assertThatThrownBy(() -> context.getEnvironment().getProperty(property))
                        .isInstanceOf(IllegalArgumentException.class);
                }
            });
        }
    }
    @Test void productionUsesProvidedCredentialsWithMysqlMigrations() {
        runner.withPropertyValues("spring.profiles.active=prod,mysql", "DB_URL=jdbc:mysql://db.example/template",
                "DB_USERNAME=app", "DB_PASSWORD=test-only-value").run(context -> {
            assertThat(context.getEnvironment().getProperty("spring.datasource.url")).isEqualTo("jdbc:mysql://db.example/template");
            assertThat(context.getEnvironment().getProperty("spring.datasource.username")).isEqualTo("app");
            assertThat(context.getEnvironment().getProperty("spring.datasource.password")).isEqualTo("test-only-value");
            assertThat(context.getEnvironment().getProperty("spring.flyway.locations")).isEqualTo("classpath:db/mysql");
        });
    }
}
