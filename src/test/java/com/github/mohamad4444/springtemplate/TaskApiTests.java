package com.github.mohamad4444.springtemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mohamad4444.springtemplate.task.TaskRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskApiTests {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired TaskRepository tasks;
    @Autowired JdbcTemplate jdbc;
    @BeforeEach void clearTasks() { tasks.deleteAll(); }

    private String createTask(String username) throws Exception {
        var result = mvc.perform(post("/api/v1/tasks").with(user(username).authorities(() -> "WRITER"))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Learn Spring\",\"description\":\"Build a template\"}"))
            .andExpect(status().isCreated()).andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.status").value("TODO"))
            .andExpect(jsonPath("$.owner").value(username))
            .andReturn();
        return json.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }
    @Test void publicHealthAndHomeButApiRequiresAuthentication() throws Exception {
        mvc.perform(get("/actuator/health")).andExpect(status().isOk());
        mvc.perform(get("/")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/tasks")).andExpect(status().isUnauthorized())
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
        mvc.perform(get("/access").with(user("writer"))).andExpect(status().isNotFound());
    }
    @Test void crudLifecycleAndOptimisticConcurrency() throws Exception {
        String id = createTask("writer");
        mvc.perform(get("/api/v1/tasks/" + id).with(user("writer")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.version").value(0));
        String update = "{\"title\":\"Done\",\"description\":\"Tested\",\"status\":\"DONE\",\"version\":0}";
        mvc.perform(put("/api/v1/tasks/" + id).with(user("writer").authorities(() -> "WRITER"))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(update))
            .andExpect(status().isOk()).andExpect(jsonPath("$.version").value(1));
        mvc.perform(put("/api/v1/tasks/" + id).with(user("writer").authorities(() -> "WRITER"))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(update))
            .andExpect(status().isConflict()).andExpect(jsonPath("$.status").value(409));
        mvc.perform(delete("/api/v1/tasks/" + id).with(user("writer").authorities(() -> "WRITER")).with(csrf()))
            .andExpect(status().isNoContent());
        mvc.perform(get("/api/v1/tasks/" + id).with(user("writer"))).andExpect(status().isNotFound());
    }
    @Test void usersOnlySeeTheirOwnTasksAndAdminCanManageAll() throws Exception {
        String id = createTask("alice");
        createTask("bob");
        mvc.perform(get("/api/v1/tasks/" + id).with(user("bob"))).andExpect(status().isNotFound());
        mvc.perform(put("/api/v1/tasks/" + id).with(user("bob").authorities(() -> "WRITER"))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Stolen\",\"description\":\"\",\"status\":\"DONE\",\"version\":0}"))
            .andExpect(status().isNotFound());
        mvc.perform(delete("/api/v1/tasks/" + id).with(user("bob").authorities(() -> "WRITER")).with(csrf()))
            .andExpect(status().isNotFound());
        mvc.perform(get("/api/v1/tasks").with(user("bob"))).andExpect(jsonPath("$.totalElements").value(1));
        mvc.perform(get("/api/v1/tasks").with(user("admin").authorities(() -> "ADMIN")))
            .andExpect(jsonPath("$.totalElements").value(2));
        mvc.perform(delete("/api/v1/tasks/" + id).with(user("admin").authorities(() -> "ADMIN")).with(csrf()))
            .andExpect(status().isNoContent());
    }
    @Test void validatesInputAndProtectsWritesAndMetrics() throws Exception {
        mvc.perform(post("/api/v1/tasks").with(user("writer").authorities(() -> "WRITER"))
                .contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"A\",\"description\":\"B\"}"))
            .andExpect(status().isForbidden());
        mvc.perform(post("/api/v1/tasks").with(user("reader")).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"A\",\"description\":\"B\"}"))
            .andExpect(status().isForbidden());
        mvc.perform(post("/api/v1/tasks").with(user("writer").authorities(() -> "WRITER")).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content("""
                {"title":"  ","description":"B"}
                """))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].field").value("title"));
        mvc.perform(get("/api/v1/tasks?sort=password").with(user("writer"))).andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/tasks?status=UNKNOWN").with(user("writer"))).andExpect(status().isBadRequest());
        mvc.perform(get("/actuator/prometheus").with(user("reader"))).andExpect(status().isForbidden());
    }
    @Test void filtersPagesAndBoundsPageSize() throws Exception {
        createTask("writer"); createTask("writer");
        mvc.perform(get("/api/v1/tasks?status=TODO&size=1").with(user("writer")))
            .andExpect(jsonPath("$.content.length()").value(1)).andExpect(jsonPath("$.totalPages").value(2));
        mvc.perform(get("/api/v1/tasks?status=DONE&size=10000").with(user("writer")))
            .andExpect(jsonPath("$.size").value(100)).andExpect(jsonPath("$.totalElements").value(0));
    }
    @Test void tiedSortValuesHavePredictablePageBoundaries() throws Exception {
        // Identical titles and timestamps make the unique tie-breaker observable.
        for (int number : new int[] {3, 1, 4, 2}) {
            jdbc.update("""
                    insert into task (id, title, description, status, owner, version, created_at, updated_at)
                    values (?, 'Same title', '', 'TODO', 'writer', 0, current_timestamp, current_timestamp)
                    """, java.util.UUID.fromString("00000000-0000-0000-0000-00000000000" + number));
        }
        jdbc.update("update task set created_at = timestamp '2026-01-01 00:00:00'");

        for (String sort : new String[] {"createdAt,desc", "title,desc", "status,asc"}) {
            mvc.perform(get("/api/v1/tasks").param("sort", sort).param("size", "2").with(user("writer")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("00000000-0000-0000-0000-000000000001"))
                .andExpect(jsonPath("$.content[1].id").value("00000000-0000-0000-0000-000000000002"));
            mvc.perform(get("/api/v1/tasks").param("sort", sort).param("size", "2").param("page", "1")
                    .with(user("writer")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("00000000-0000-0000-0000-000000000003"))
                .andExpect(jsonPath("$.content[1].id").value("00000000-0000-0000-0000-000000000004"));
        }
        mvc.perform(get("/api/v1/tasks").param("sort", "title,desc").param("sort", "id,desc")
                .param("size", "2").with(user("writer")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value("00000000-0000-0000-0000-000000000004"))
            .andExpect(jsonPath("$.content[1].id").value("00000000-0000-0000-0000-000000000003"));
    }
    @Test void openApiIsGeneratedForTheVersionedApi() throws Exception {
        mvc.perform(get("/v3/api-docs").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.paths['/api/v1/tasks']").exists());
    }
    @Test void academicFrontendIsAbsentFromDefaultApplication() throws Exception {
        mvc.perform(get("/academic/notizen/demo.html").with(user("writer"))).andExpect(status().isNotFound());
        mvc.perform(get("/academic/notizen/js/my_scripts.js").with(user("writer"))).andExpect(status().isNotFound());
    }
    @Test void requestIdAndCsrfTokenAreAvailable() throws Exception {
        mvc.perform(get("/hello").header("X-Request-Id", "test-123"))
            .andExpect(header().string("X-Request-Id", "test-123"));
        mvc.perform(get("/api/v1/csrf").with(user("writer")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.token").isNotEmpty());
        assertThat(tasks.count()).isZero();
    }
}
