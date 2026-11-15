package com.github.mohamad4444.springtemplate.web;

import com.github.mohamad4444.springtemplate.model.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    ProblemDetail notFound(NotFoundException exception) { return problem(HttpStatus.NOT_FOUND, exception.getMessage()); }
    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail badRequest(IllegalArgumentException exception) { return problem(HttpStatus.BAD_REQUEST, "Invalid request parameters"); }
    @ExceptionHandler(OptimisticLockingFailureException.class)
    ProblemDetail conflict(OptimisticLockingFailureException exception) {
        return problem(HttpStatus.CONFLICT, "Task changed; reload it and retry with the current version");
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail integrity(DataIntegrityViolationException exception) { return problem(HttpStatus.CONFLICT, "Resource conflicts with existing data"); }
    @ExceptionHandler(AccessDeniedException.class)
    ProblemDetail forbidden(AccessDeniedException exception) { return problem(HttpStatus.FORBIDDEN, "Access denied"); }
    @ExceptionHandler(RestClientException.class)
    ProblemDetail upstream(RestClientException exception) { return problem(HttpStatus.BAD_GATEWAY, "External service unavailable"); }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var detail = problem(HttpStatus.BAD_REQUEST, "Request validation failed");
        detail.setProperty("errors", exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage())).toList());
        return handleExceptionInternal(exception, detail, headers, status, request);
    }
    private ProblemDetail problem(HttpStatus status, String detail) { return ProblemDetail.forStatusAndDetail(status, detail); }
    record FieldError(String field, String message) { }
}
