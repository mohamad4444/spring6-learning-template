package com.github.mohamad4444.springtemplate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("academic")
class AcademicProfileTests {
    @Autowired MockMvc mvc;
    @Autowired com.github.mohamad4444.springtemplate.model.dbaccess.DBAccessService database;
    @Test void manualLoginChecksCorrectHashAndRejectsWrongPassword() throws Exception {
        String username = "manual-" + java.util.UUID.randomUUID();
        database.createBenutzer("Test", "User", username, "correct-password");
        mvc.perform(post("/access").with(user("learner")).with(csrf()).contentType("application/json")
                .content("{\"benutzername\":\"" + username + "\",\"password\":\"correct-password\"}"))
            .andExpect(status().isOk());
        mvc.perform(post("/access").with(user("learner")).with(csrf()).contentType("application/json")
                .content("{\"benutzername\":\"" + username + "\",\"password\":\"wrong-password\"}"))
            .andExpect(status().isForbidden());
    }
    @Test void mergedBrowserFrontendAndAssetsAreAvailable() throws Exception {
        mvc.perform(get("/academic/notizen/demo.html").with(user("learner")))
            .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("Benutzer-Demo")));
        mvc.perform(get("/academic/notizen/js/my_scripts.js").with(user("learner")))
            .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("/api/v1/csrf")));
        mvc.perform(get("/academic/notizen/css/mystyle.css").with(user("learner"))).andExpect(status().isOk());
    }
    @Test void mergedBrowserApiSupportsRegistrationUpdateLogoutAndDeletion() throws Exception {
        String username = "browser-" + java.util.UUID.randomUUID();
        var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        String registration = mapper.writeValueAsString(java.util.Map.of(
                "vorname", "Browser", "nachname", "User", "benutzername", username, "password", "original-password"));
        var created = mvc.perform(post("/benutzer").with(user("learner")).with(csrf())
                .contentType("application/json").content(registration))
            .andExpect(status().isOk()).andReturn();
        var body = mapper.readTree(created.getResponse().getContentAsString());
        String id = body.get("id").asText();
        String token = body.get("credential").get("accessToken").asText();
        mvc.perform(get("/benutzer").with(user("learner")).header("accessToken", token))
            .andExpect(status().isOk());
        mvc.perform(put("/benutzer/" + id).with(user("learner")).with(csrf()).header("accessToken", token)
                .contentType("application/json").content("""
                {"vorname":"Updated","nachname":"User","password":"changed-password"}
                """))
            .andExpect(status().isOk()).andExpect(jsonPath("$.vorname").value("Updated"));
        mvc.perform(delete("/access").with(user("learner")).with(csrf()).header("accessToken", token))
            .andExpect(status().isOk()).andExpect(content().string("true"));
        mvc.perform(get("/benutzer").with(user("learner")).header("accessToken", token))
            .andExpect(status().isUnauthorized());
        var loggedIn = mvc.perform(post("/access").with(user("learner")).with(csrf()).contentType("application/json")
                .content(mapper.writeValueAsString(java.util.Map.of("benutzername", username, "password", "changed-password"))))
            .andExpect(status().isOk()).andReturn();
        String nextToken = mapper.readTree(loggedIn.getResponse().getContentAsString()).get("credential").get("accessToken").asText();
        mvc.perform(delete("/benutzer/" + id).with(user("learner")).with(csrf()).header("accessToken", nextToken))
            .andExpect(status().isOk());
    }
    @Test void learningExamplesAndSeparateViewsStartTogether() throws Exception {
        mvc.perform(get("/ui/notes").with(user("learner"))).andExpect(status().isOk());
        mvc.perform(get("/ui/secured-notes").with(user("learner"))).andExpect(status().isOk());
        mvc.perform(get("/jpa/notes").with(user("learner"))).andExpect(status().isOk());
    }
    @Test void seededAccountCanAuthenticate() throws Exception {
        mvc.perform(get("/api/v1/me").with(httpBasic("ester.nutzer@hs-kl.de", "user1-secret")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.username").value("ester.nutzer@hs-kl.de"));
        mvc.perform(get("/api/v1/me").with(httpBasic("ester.nutzer@hs-kl.de", "wrong")))
            .andExpect(status().isUnauthorized());
    }
}
