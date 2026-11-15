package com.github.mohamad4444.springtemplate.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;

public final class TaskDtos {
    private TaskDtos() { }

    public record Create(
            @NotBlank @Size(max = 120) String title,
            @NotNull @Size(max = 2000) String description) { }

    public record Update(
            @NotBlank @Size(max = 120) String title,
            @NotNull @Size(max = 2000) String description,
            @NotNull Task.Status status,
            @NotNull @PositiveOrZero Long version) { }

    public record Response(UUID id, String title, String description, Task.Status status,
                           String owner, long version, Instant createdAt, Instant updatedAt) {
        public static Response from(Task task) {
            return new Response(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(),
                    task.getOwner(), task.getVersion(), task.getCreatedAt(), task.getUpdatedAt());
        }
    }

    public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {
        public static <T> PageResponse<T> from(Page<T> page) {
            return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(),
                    page.getTotalElements(), page.getTotalPages());
        }
    }
}
