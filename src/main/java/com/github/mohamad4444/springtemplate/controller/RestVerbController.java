package com.github.mohamad4444.springtemplate.controller;
import com.github.mohamad4444.springtemplate.model.RestExampleRequestBody;
import com.github.mohamad4444.springtemplate.model.RestExampleResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Verb Example Controller
 * 
 * <p>
 * This controller demonstrates fundamental REST API concepts in Spring Boot:
 * <ul>
 * <li>Different HTTP methods (GET, POST)</li>
 * <li>Content negotiation (JSON vs XML)</li>
 * <li>Custom HTTP status codes with ResponseEntity</li>
 * <li>Request parameter binding (@RequestParam)</li>
 * <li>Request body binding (@RequestBody)</li>
 * </ul>
 * 
 * <p>
 * <b>Spring Boot Principles Demonstrated:</b>
 * <ul>
 * <li>{@code @RestController}: Combines @Controller and @ResponseBody for REST
 * endpoints</li>
 * <li>{@code @GetMapping/@PostMapping}: HTTP method-specific routing</li>
 * <li>{@code produces}: Specifies response content type (JSON/XML)</li>
 * <li>{@code ResponseEntity}: Full control over HTTP response (status, headers,
 * body)</li>
 * <li>{@code @RequestParam}: Binds query parameters from URL</li>
 * <li>{@code @RequestBody}: Binds JSON request body to Java object</li>
 * </ul>
 * 
 * <p>
 * <b>Testing Examples:</b>
 * These routes require authentication. POST requests also require a CSRF token
 * and its matching session cookie; see docs/FIRST_STEPS.md for the api helper,
 * credentials, base URL and CSRF variables used in the POST examples.
 * 
 * <pre>
 * # Test GET request returning JSON
 * curl -u ester.nutzer@hs-kl.de:user1-secret http://localhost:8080/rest/piplup
 * 
 * # Test GET request returning XML
 * curl -u ester.nutzer@hs-kl.de:user1-secret http://localhost:8080/piplup-xml
 * 
 * # Test custom HTTP status code (418 I'm a teapot)
 * curl -v -u ester.nutzer@hs-kl.de:user1-secret http://localhost:8080/piplup-teapot
 * 
 * # Test POST with query parameters
 * api -X POST -H "$csrf_header: $csrf_token" "$base/rest/me?name=John&age=25&favorites=Java&favorites=Spring"
 * 
 * # Test POST with JSON body
 * api -X POST -H "$csrf_header: $csrf_token" "$base/me-body" \
 *   -H "Content-Type: application/json" \
 *   -d '{"name":"John","age":25,"favoriteThings":["Java","Spring"]}'
 * </pre>
 * 
 * @see RestExampleResponse
 * @see RestExampleRequestBody
 * @author Spring Boot Template
 */
@RestController
public class RestVerbController {

	/**
	 * Simple GET endpoint returning JSON.
	 * 
	 * <p>
	 * Demonstrates:
	 * <ul>
	 * <li>Basic {@code @GetMapping} usage</li>
	 * <li>Automatic JSON serialization with Jackson</li>
	 * <li>Content type specification with {@code produces}</li>
	 * </ul>
	 * 
	 * <p>
	 * Spring Boot automatically converts the returned object to JSON using Jackson.
	 * The {@code produces} parameter ensures the response Content-Type is
	 * application/json.
	 * 
	 * @return RestExampleResponse object (automatically converted to JSON)
	 */
	@GetMapping(value = "/rest/piplup", produces = "application/json")
	public RestExampleResponse handleGet() {
		return new RestExampleResponse("Piplup", 8, List.of("Fish", "Swimming"));
	}

	/**
	 * GET endpoint demonstrating custom HTTP status codes with ResponseEntity.
	 * 
	 * <p>
	 * Demonstrates:
	 * <ul>
	 * <li>Using {@code ResponseEntity} for full HTTP response control</li>
	 * <li>Setting custom HTTP status codes (418 I'm a teapot)</li>
	 * <li>Separation of response body and HTTP metadata</li>
	 * </ul>
	 * 
	 * <p>
	 * {@code ResponseEntity} allows you to control:
	 * <ul>
	 * <li>HTTP status code (here: 418 I'm a teapot - a humorous status code from
	 * RFC 2324)</li>
	 * <li>HTTP headers</li>
	 * <li>Response body</li>
	 * </ul>
	 * 
	 * @return ResponseEntity containing RestExampleResponse with 418 status code
	 */
	@GetMapping(value = "/piplup-teapot")
	public ResponseEntity<RestExampleResponse> handleGetResponseEntity() {
		final var response = new RestExampleResponse("Piplup", 8, List.of("Fish", "Swimming"));
		return new ResponseEntity<RestExampleResponse>(response, HttpStatus.I_AM_A_TEAPOT);
	}

	/**
	 * GET endpoint returning XML instead of JSON.
	 * 
	 * <p>
	 * Demonstrates:
	 * <ul>
	 * <li>Content negotiation - same controller can return different formats</li>
	 * <li>XML serialization with Jackson XML extension</li>
	 * <li>{@code produces = "application/xml"} to specify XML response</li>
	 * </ul>
	 * 
	 * <p>
	 * Requires {@code jackson-dataformat-xml} dependency in pom.xml.
	 * Spring Boot automatically uses Jackson XML to serialize the response to XML
	 * format.
	 * 
	 * @return RestExampleResponse object (automatically converted to XML)
	 */
	@GetMapping(value = "/piplup-xml", produces = "application/xml")
	public RestExampleResponse handleGetXML() {
		return new RestExampleResponse("Piplup", 8, List.of("Fish", "Swimming"));
	}

	/**
	 * POST endpoint with query parameters using @RequestParam.
	 * 
	 * <p>
	 * Demonstrates:
	 * <ul>
	 * <li>{@code @PostMapping} for POST requests</li>
	 * <li>{@code @RequestParam} to bind query parameters from URL</li>
	 * <li>Automatic type conversion (String to int for age)</li>
	 * <li>List parameter binding (multiple favorites values)</li>
	 * </ul>
	 * 
	 * <p>
	 * Query parameters are extracted from the URL:
	 * {@code /rest/me?name=John&age=25&favorites=Java&favorites=Spring}
	 * 
	 * <p>
	 * Spring Boot automatically:
	 * <ul>
	 * <li>Extracts each parameter from the URL</li>
	 * <li>Converts types (String to int)</li>
	 * <li>Aggregates multiple values into a List</li>
	 * <li>Returns 400 Bad Request if required parameters are missing or
	 * invalid</li>
	 * </ul>
	 * 
	 * @param name      person's name (required query parameter)
	 * @param age       person's age (required query parameter, automatically
	 *                  converted to int)
	 * @param favorites list of favorite things (required query parameter, can
	 *                  appear multiple times)
	 * @return RestExampleResponse containing the provided data
	 */
	@PostMapping("/rest/me")
	public RestExampleResponse handlePost(@RequestParam String name, @RequestParam int age,
			@RequestParam List<String> favorites) {
		return new RestExampleResponse(name, age, favorites);
	}

	/**
	 * POST endpoint with JSON request body using @RequestBody.
	 * 
	 * <p>
	 * Demonstrates:
	 * <ul>
	 * <li>{@code @RequestBody} to bind JSON payload to Java object</li>
	 * <li>Automatic JSON deserialization with Jackson</li>
	 * <li>Type-safe request handling with DTOs</li>
	 * <li>Java Records as DTOs</li>
	 * </ul>
	 * 
	 * <p>
	 * The request body should be JSON matching the RestExampleRequestBody structure:
	 * 
	 * <pre>
	 * {
	 *   "name": "John",
	 *   "age": 25,
	 *   "favoriteThings": ["Java", "Spring"]
	 * }
	 * </pre>
	 * 
	 * <p>
	 * Spring Boot automatically:
	 * <ul>
	 * <li>Deserializes JSON to {@link RestExampleRequestBody}</li>
	 * <li>Validates JSON structure</li>
	 * <li>Returns 400 Bad Request if JSON is invalid</li>
	 * </ul>
	 * 
	 * <p>
	 * <b>Best Practice:</b> Using {@code @RequestBody} with DTOs is preferred over
	 * {@code @RequestParam} for complex data structures, as it provides:
	 * <ul>
	 * <li>Type safety</li>
	 * <li>Easier validation</li>
	 * <li>Cleaner API design</li>
	 * <li>Better documentation</li>
	 * </ul>
	 * 
	 * @param body RestExampleRequestBody deserialized from JSON request body
	 * @return RestExampleResponse containing the provided data
	 * @see RestExampleRequestBody
	 */
	@PostMapping("/me-body")
	public RestExampleResponse handlePostBody(@RequestBody RestExampleRequestBody body) {
		return new RestExampleResponse(body.name(), body.age(), body.favoriteThings());
	}
}
