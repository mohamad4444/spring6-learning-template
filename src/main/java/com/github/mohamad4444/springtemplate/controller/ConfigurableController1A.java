package com.github.mohamad4444.springtemplate.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// config exercise 1a
@RestController
public class ConfigurableController1A {
	// message2 is injected in the constructor
	public ConfigurableController1A(@Value("${controller.othermessage}") String otherMessage) {
		this.otherMessage = otherMessage;
	}

	private String otherMessage;

	// message is injected into the field
	@Value("${controller.message}")
	private String message;

	@GetMapping("/example")
	public String handleGet() {
		return message;
	}

	@GetMapping("/example2")
	public String handleGet2() {
		return otherMessage;
	}
}
