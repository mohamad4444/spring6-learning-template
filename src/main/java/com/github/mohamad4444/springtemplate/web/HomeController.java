package com.github.mohamad4444.springtemplate.web;

import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final Environment environment;

    public HomeController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("springBootVersion", SpringBootVersion.getVersion());
        model.addAttribute("springFrameworkVersion", SpringVersion.getVersion());
        model.addAttribute("academicEnabled", environment.acceptsProfiles(Profiles.of("academic")));
        return "index";
    }
}
