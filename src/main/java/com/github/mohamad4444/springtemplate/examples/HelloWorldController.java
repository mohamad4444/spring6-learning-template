package com.github.mohamad4444.springtemplate.examples;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    class First {
        @GetMapping("/hello")
        public String helloWorld() {
            return "Hello World!";
        }
    }
}
