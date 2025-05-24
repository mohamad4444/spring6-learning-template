package com.github.mohamad4444.springtemplate.examples.config.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//config exercise 1b
@RestController
public class ConfigurableController1B {
	Logger logger = LoggerFactory.getLogger(ConfigurableController1B.class);
	
	// message2 is injected in the constructor
	public ConfigurableController1B(Controller1BConfig config) {
		this.config = config;
	}

	private Controller1BConfig config;

	@GetMapping("/hi")
	public String handleGet() {
		logger.info(config.getLog());
		
		return config.getMessage();
	}
}
