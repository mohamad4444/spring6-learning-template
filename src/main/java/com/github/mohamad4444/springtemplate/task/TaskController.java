package com.github.mohamad4444.springtemplate.task;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@io.swagger.v3.oas.annotations.security.SecurityRequirements({
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "basicAuth"),
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
})
@Tag(name = "Tasks", description = "Owner-scoped, paginated task management")
public class TaskController {
    private static final Set<String> SORT_FIELDS = Set.of("id", "title", "status", "createdAt", "updatedAt");

    private final TaskService tasks;

    public TaskController(TaskService tasks) {
        this.tasks = tasks;
    }

    @GetMapping
    public TaskDtos.PageResponse<TaskDtos.Response> list(@RequestParam(required = false) Task.Status status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication user) {
        if (pageable.getSort().stream().anyMatch(order -> !SORT_FIELDS.contains(order.getProperty()))) {
            throw new IllegalArgumentException("Unsupported sort field");
        }
        return tasks.list(status, pageable, user);
    }

    @GetMapping("/{id}")
    public TaskDtos.Response get(@PathVariable UUID id, Authentication user) {
        return tasks.get(id, user);
    }

    @PostMapping
    public ResponseEntity<TaskDtos.Response> create(@Valid @RequestBody TaskDtos.Create body, Authentication user) {
        var result = tasks.create(body, user);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @PutMapping("/{id}")
    public TaskDtos.Response update(@PathVariable UUID id, @Valid @RequestBody TaskDtos.Update body, Authentication user) {
        return tasks.update(id, body, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication user) {
        tasks.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
