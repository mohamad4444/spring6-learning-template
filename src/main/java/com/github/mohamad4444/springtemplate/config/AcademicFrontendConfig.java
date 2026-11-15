package com.github.mohamad4444.springtemplate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Serves the merged browser exercise only while its backend is enabled. */
@Configuration
@Profile("academic")
public class AcademicFrontendConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/academic/notizen/**")
                .addResourceLocations("classpath:/academic/notizen/");
    }
}
