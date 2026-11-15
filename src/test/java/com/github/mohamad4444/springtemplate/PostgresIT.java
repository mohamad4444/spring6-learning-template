package com.github.mohamad4444.springtemplate;

import com.github.mohamad4444.springtemplate.task.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class PostgresIT {
    @Container @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");
    @Autowired TaskRepository tasks;
    @Test void migrationsAndHibernateMappingsWorkOnPostgres() {
        Task saved = tasks.saveAndFlush(new Task("PostgreSQL", "Real database integration", "test"));
        assertThat(tasks.findById(saved.getId())).isPresent();
        tasks.delete(saved);
    }
}
