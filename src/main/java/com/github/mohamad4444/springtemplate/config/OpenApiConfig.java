package com.github.mohamad4444.springtemplate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI api() {
        return new OpenAPI().info(new Info().title("Spring 6 SpringBoot 3.5 learning API").version("v1")
                .description("Combined college lessons in REST, persistence, security and integrations. "
                    + "Tasks require authentication. Session requests also require a CSRF token for writes."))
            .components(new Components()
                .addSecuritySchemes("basicAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic"))
                .addSecuritySchemes("bearerAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
