package com.github.mohamad4444.springtemplate.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Profile;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SessionController {
    @GetMapping("/me")
    public CurrentUser me(Authentication user) {
        return new CurrentUser(user.getName(), user.getAuthorities().stream().map(Object::toString).toList());
    }
    record CurrentUser(String username, List<String> authorities) { }
}
