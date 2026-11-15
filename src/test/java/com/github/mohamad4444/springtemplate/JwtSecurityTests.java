package com.github.mohamad4444.springtemplate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.Instant;
import java.util.List;

@SpringBootTest(properties = {"spring.security.oauth2.resourceserver.jwt.issuer-uri=https://issuer.example", "spring.security.oauth2.resourceserver.jwt.audiences=springtemplate"})
@AutoConfigureMockMvc
@ActiveProfiles({"test", "jwt"})
class JwtSecurityTests {
    @Autowired MockMvc mvc;
    @MockitoBean JwtDecoder decoder;

    @Test void signedBearerIdentityUsesIssuerRolesAndDoesNotRequireCsrf() throws Exception {
        when(decoder.decode("writer-token")).thenReturn(Jwt.withTokenValue("writer-token")
            .header("alg", "RS256").subject("jwt-writer").issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(120)).claim("roles", List.of("WRITER")).build());
        var result = mvc.perform(post("/api/v1/tasks").header("Authorization", "Bearer writer-token")
                .contentType("application/json").content("{\"title\":\"JWT task\",\"description\":\"Bearer only\"}"))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.owner").value("jwt-writer")).andReturn();
        var body = new com.fasterxml.jackson.databind.ObjectMapper().readTree(result.getResponse().getContentAsString());
        mvc.perform(delete("/api/v1/tasks/" + body.get("id").asText()).header("Authorization", "Bearer writer-token"))
            .andExpect(status().isNoContent());
    }
    @Test void refusesInvalidTokensAndBasicCredentials() throws Exception {
        when(decoder.decode("invalid")).thenThrow(new BadJwtException("Invalid signature"));
        mvc.perform(get("/api/v1/me").header("Authorization", "Bearer invalid")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/v1/me").with(httpBasic("demo", "demo"))).andExpect(status().isUnauthorized());
    }
    @Test void bearerWithoutWriterRoleCannotCreate() throws Exception {
        when(decoder.decode("reader-token")).thenReturn(Jwt.withTokenValue("reader-token")
            .header("alg", "RS256").subject("reader").claim("roles", List.of("UNTRUSTED", "ROLE_ADMIN")).build());
        mvc.perform(post("/api/v1/tasks").header("Authorization", "Bearer reader-token")
                .contentType("application/json").content("{\"title\":\"A\",\"description\":\"B\"}"))
            .andExpect(status().isForbidden());
    }
}
