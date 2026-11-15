package com.github.mohamad4444.springtemplate;

import com.github.mohamad4444.springtemplate.task.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test", "mysql"})
@Testcontainers
class MySqlIT {
    @Container @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4");
    @Autowired TaskRepository tasks;
    @Test void migrationsAndHibernateMappingsWorkOnMySql() {
        Task saved = tasks.saveAndFlush(new Task("MySQL", "Database integration", "test"));
        assertThat(tasks.findById(saved.getId())).isPresent();
        tasks.delete(saved);
    }
}
