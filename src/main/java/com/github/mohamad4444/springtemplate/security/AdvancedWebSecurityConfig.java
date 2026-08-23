package com.github.mohamad4444.springtemplate.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AdvancedWebSecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(authorizeHttpRequests -> 
	    	authorizeHttpRequests
	    	.requestMatchers("/swagger-ui/*")
	    	.permitAll()
	    	.requestMatchers("/actuator/health")
	    	.permitAll()
	    	.anyRequest()
	    	.authenticated()
	    )
	    .httpBasic(withDefaults())
	    .formLogin(withDefaults());
		
		return http.build();
	}
}
