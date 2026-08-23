package com.github.mohamad4444.springtemplate.model;

import java.util.List;

/**
 * Example Request Body DTO (Data Transfer Object)
 * 
 * <p>
 * This record represents the expected JSON structure for POST requests
 * with {@code @RequestBody} in the REST verb examples.
 * 
 * <p>
 * <b>Spring Boot Principles Demonstrated:</b>
 * <ul>
 * <li><b>@RequestBody Binding</b>: Jackson deserializes JSON to this
 * object</li>
 * <li><b>Java Records</b>: Clean, immutable request DTOs</li>
 * <li><b>Type Safety</b>: Compile-time validation of request structure</li>
 * </ul>
 * 
 * <p>
 * <b>Expected JSON Format:</b>
 * 
 * <pre>
 * {
 *   "name": "John",
 *   "age": 25,
 *   "favoriteThings": ["Java", "Spring", "REST APIs"]
 * }
 * </pre>
 * 
 * <p>
 * <b>Usage Example:</b>
 * 
 * <pre>
 * curl -X POST http://localhost:8080/me-body \
 *   -H "Content-Type: application/json" \
 *   -d '{"name":"John","age":25,"favoriteThings":["Java","Spring"]}'
 * </pre>
 * 
 * <p>
 * <b>Validation:</b>
 * Spring Boot automatically validates:
 * <ul>
 * <li>JSON is well-formed</li>
 * <li>Field types match (age must be a number)</li>
 * <li>Required fields are present</li>
 * <li>Returns 400 Bad Request if validation fails</li>
 * </ul>
 * 
 * <p>
 * <b>Best Practices:</b>
 * <ul>
 * <li>Use descriptive field names matching JSON conventions (camelCase)</li>
 * <li>Add {@code @Valid} annotation for bean validation if needed</li>
 * <li>Keep DTOs separate from domain entities</li>
 * <li>Use records for immutability and thread safety</li>
 * </ul>
 * 
 * @param name           person's name (required)
 * @param age            person's age in years (required, must be a valid
 *                       integer)
 * @param favoriteThings list of favorite things (required, can be empty)
 * 
 * @see RestVerbController#handlePostBody(RestExampleRequestBody)
 * @author Spring Boot Template
 */
public record RestExampleRequestBody(String name, int age, List<String> favoriteThings) {
}
