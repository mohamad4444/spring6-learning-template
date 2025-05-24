package com.github.mohamad4444.springtemplate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodMorningController {
	
	@GetMapping("/morning")
	public String goodMorning() {
		return "Good morning!";
	}
}
