package com.github.mohamad4444.springtemplate.task;

import com.github.mohamad4444.springtemplate.model.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@PreAuthorize("isAuthenticated()")
public class TaskService {
    private final TaskRepository tasks;

    public TaskService(TaskRepository tasks) {
        this.tasks = tasks;
    }

    public TaskDtos.PageResponse<TaskDtos.Response> list(Task.Status status, Pageable pageable, Authentication user) {
        // Scope the SQL query itself so page content and counts obey the same ownership rule.
        Specification<Task> spec = (root, query, cb) -> admin(user) ? cb.conjunction()
                : cb.equal(root.get("owner"), user.getName());
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        // Equal timestamps or titles need a unique tie-breaker for predictable page boundaries.
        Sort sort = pageable.getSort();
        if (sort.getOrderFor("id") == null) {
            sort = sort.and(Sort.by("id"));
        }
        Pageable orderedPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return TaskDtos.PageResponse.from(tasks.findAll(spec, orderedPage).map(TaskDtos.Response::from));
    }

    public TaskDtos.Response get(UUID id, Authentication user) {
        return TaskDtos.Response.from(ownedTask(id, user));
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('WRITER', 'ADMIN')")
    public TaskDtos.Response create(TaskDtos.Create body, Authentication user) {
        // Ownership comes from the authenticated principal, never from the request body.
        Task task = new Task(body.title().strip(), body.description(), user.getName());
        return TaskDtos.Response.from(tasks.saveAndFlush(task));
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('WRITER', 'ADMIN')")
    public TaskDtos.Response update(UUID id, TaskDtos.Update body, Authentication user) {
        Task task = ownedTask(id, user);
        if (task.getVersion() != body.version()) {
            throw new ObjectOptimisticLockingFailureException(Task.class, id);
        }
        task.update(body.title().strip(), body.description(), body.status());
        // Flush before mapping: Hibernate updates @Version and detects concurrent database writes here.
        return TaskDtos.Response.from(tasks.saveAndFlush(task));
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('WRITER', 'ADMIN')")
    public void delete(UUID id, Authentication user) {
        tasks.delete(ownedTask(id, user));
    }

    private Task ownedTask(UUID id, Authentication user) {
        Task task = tasks.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
        // Hide the existence of another user's resource.
        if (!admin(user) && !task.getOwner().equals(user.getName())) {
            throw new NotFoundException("Task not found");
        }
        return task;
    }

    private boolean admin(Authentication user) {
        return user.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }
}
