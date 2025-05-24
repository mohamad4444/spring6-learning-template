package com.github.mohamad4444.springtemplate.examples.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VerbExampleController {

	@GetMapping(value = "/piplup", produces = "application/json")
	public ExampleResponse handleGet() {
		return new ExampleResponse("Piplup", 8, List.of("Fish", "Swimming"));
	}
	
	@GetMapping(value = "/piplup-teapot")
	public ResponseEntity<ExampleResponse> handleGetResponseEntity() {
		final var response = new ExampleResponse("Piplup", 8, List.of("Fish", "Swimming"));
		return new ResponseEntity<ExampleResponse>(response, HttpStatus.I_AM_A_TEAPOT);
	}

	@GetMapping(value = "/piplup-xml", produces = "application/xml")
	public ExampleResponse handleGetXML() {
		return new ExampleResponse("Piplup", 8, List.of("Fish", "Swimming"));
	}

	@PostMapping("/me")
	public ExampleResponse handlePost(@RequestParam String name, @RequestParam int age, @RequestParam List<String> favorites) {
		return new ExampleResponse(name, age, favorites);
	}

	@PostMapping("/me-body")
	public ExampleResponse handlePostBody(@RequestBody ExampleRequestBody body) {
		return new ExampleResponse(body.name(), body.age(), body.favoriteThings());
	}
}
