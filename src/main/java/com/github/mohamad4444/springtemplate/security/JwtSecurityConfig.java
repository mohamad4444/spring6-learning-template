package com.github.mohamad4444.springtemplate.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import java.util.List;

/** Issuer and audience are mandatory in the jwt profile. The identity provider owns roles. */
@Configuration
@Profile("jwt")
public class JwtSecurityConfig {
    @Bean
    SecurityFilterChain bearerSecurity(HttpSecurity http) throws Exception {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            return roles == null ? List.of() : roles.stream()
                    .filter(role -> role.equals("ADMIN") || role.equals("WRITER"))
                    .map(role -> (org.springframework.security.core.GrantedAuthority) new SimpleGrantedAuthority(role))
                    .toList();
        });
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Bearer credentials are supplied explicitly; this chain never accepts sessions or Basic auth.
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/hello", "/morning", "/error", "/actuator/health", "/actuator/health/**",
                    "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/**").hasAuthority("ADMIN")
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)));
        return http.build();
    }
}
