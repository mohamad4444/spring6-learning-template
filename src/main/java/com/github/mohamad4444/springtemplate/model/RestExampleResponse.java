package com.github.mohamad4444.springtemplate.model;

import java.util.List;

/**
 * Example Response DTO (Data Transfer Object)
 * 
 * <p>
 * This record represents the response format for the REST verb examples.
 * It demonstrates using Java Records as DTOs in Spring Boot REST APIs.
 * 
 * <p>
 * <b>Spring Boot Principles Demonstrated:</b>
 * <ul>
 * <li><b>Java Records</b>: Immutable data carriers (Java 14+)</li>
 * <li><b>Automatic JSON Serialization</b>: Jackson converts this to JSON
 * automatically</li>
 * <li><b>DTO Pattern</b>: Separates API contract from domain model</li>
 * </ul>
 * 
 * <p>
 * <b>Benefits of Records for DTOs:</b>
 * <ul>
 * <li>Immutable by default (thread-safe)</li>
 * <li>Automatic getters, equals(), hashCode(), toString()</li>
 * <li>Compact syntax - no boilerplate code needed</li>
 * <li>Perfect for simple data carriers</li>
 * </ul>
 * 
 * <p>
 * <b>JSON Representation:</b>
 * 
 * <pre>
 * {
 *   "name": "Piplup",
 *   "age": 8,
 *   "favoriteThings": ["Fish", "Swimming"]
 * }
 * </pre>
 * 
 * <p>
 * Jackson automatically:
 * <ul>
 * <li>Uses record component names as JSON keys</li>
 * <li>Serializes to JSON for responses</li>
 * <li>Deserializes from JSON for requests</li>
 * </ul>
 * 
 * @param name           person's or character's name
 * @param age            age in years
 * @param favoriteThings list of favorite things
 * 
 * @see RestVerbController
 * @author Spring Boot Template
 */
public record RestExampleResponse(String name, int age, List<String> favoriteThings) {
}
