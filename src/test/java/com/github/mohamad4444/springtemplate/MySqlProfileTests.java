package com.github.mohamad4444.springtemplate;

import com.github.mohamad4444.springtemplate.task.*;
import com.github.mohamad4444.springtemplate.model.dbaccess.DBAccessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

/** Checks the MySQL schema and Hibernate dialect using H2 emulation; MySqlIT covers the actual engine. */
@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:mysqltest;MODE=MySQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa", "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect"
})
@ActiveProfiles({"test", "mysql"})
class MySqlProfileTests {
    @Autowired TaskRepository tasks;
    @Autowired DBAccessService database;
    @Test void mysqlSchemaSupportsUuidEnumAndBinaryPasswordFields() {
        Task task = tasks.saveAndFlush(new Task("MySQL schema", "Dialect compatibility", "tester"));
        task.update(task.getTitle(), task.getDescription(), Task.Status.DONE);
        task = tasks.saveAndFlush(task);
        assertThat(tasks.findById(task.getId()).orElseThrow().getStatus()).isEqualTo(Task.Status.DONE);
        assertThat(task.getVersion()).isEqualTo(1);
        String username = "mysql-" + java.util.UUID.randomUUID();
        var user = database.createBenutzer("MySQL", "Test", username, "test-password");
        assertThat(database.getBenutzerById(user.getId()).getPasswordHash()).isNotEmpty();
        database.deleteBenutzer(user.getId());
        tasks.delete(task);
    }
}
