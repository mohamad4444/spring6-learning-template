package com.github.mohamad4444.springtemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@SpringBootApplication
//@SpringBootApplication(scanBasePackages = "de.other") // while possible, its better to place all packages below the class annotated with @SpringBootApplication
public class SpringtemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringtemplateApplication.class, args);
	}
	@RestController
	class First {

		@GetMapping("/hello")
		public String helloWorld() {
			return "Hello World!";
		}
	}
}
