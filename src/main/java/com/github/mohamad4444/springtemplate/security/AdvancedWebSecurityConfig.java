package com.github.mohamad4444.springtemplate.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.*;
import org.springframework.http.ProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class AdvancedWebSecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Profile("!jwt")
    SecurityFilterChain sessionSecurity(HttpSecurity http, ObjectMapper mapper) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/hello", "/morning", "/error", "/actuator/health", "/actuator/health/**",
                        "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/**").hasAuthority("ADMIN")
                .anyRequest().authenticated())
            .httpBasic(withDefaults()).formLogin(withDefaults())
            .exceptionHandling(errors -> errors
                .defaultAuthenticationEntryPointFor((request, response, exception) -> {
                    response.setHeader("WWW-Authenticate", "Basic realm=\"springtemplate\"");
                    problem(response, mapper, HttpStatus.UNAUTHORIZED, "Authentication required");
                }, new AntPathRequestMatcher("/api/**"))
                .accessDeniedHandler((request, response, exception) ->
                    problem(response, mapper, HttpStatus.FORBIDDEN, "Access denied or CSRF token missing")));
        // CSRF protection remains enabled, including for authenticated JSON requests.
        return http.build();
    }

    static void problem(HttpServletResponse response, ObjectMapper mapper, HttpStatus status, String detail)
            throws java.io.IOException {
        response.setStatus(status.value());
        response.setContentType("application/problem+json");
        mapper.writeValue(response.getOutputStream(), ProblemDetail.forStatusAndDetail(status, detail));
    }
}
