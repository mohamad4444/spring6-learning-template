package com.github.mohamad4444.springtemplate.web;

import org.springframework.context.annotation.Profile;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@Profile("!jwt")
public class CsrfController {
    @GetMapping("/api/v1/csrf")
    public CsrfToken csrf(CsrfToken token) { return token; }
}
