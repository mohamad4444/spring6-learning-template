# College lessons and learning path

This project combines lessons and exercises from college in one application. Start with the [college lesson index](../README.md#college-lesson-index) and the basic controller examples below. The sequence is a suggested way to study the material.

For a guided exercise that connects the topics, follow [your first task API lesson](FIRST_STEPS.md). It includes copyable requests, expected responses, source links, and debugger checkpoints. Then choose [a practice exercise](EXERCISES.md) with acceptance checks. Keep [troubleshooting](TROUBLESHOOTING.md) nearby while setting up.

Use this sequence to choose what to study next:

| Stage | Goal | Evidence you understand it |
| --- | --- | --- |
| HTTP and MVC | Bind parameters/JSON and choose response status | Explain a 201 response and its Location header |
| Validation and DTOs | Define the public API separately from entities | Trigger a field validation error without writing a row |
| Services and persistence | Follow transactions, repository queries, and Flyway | Trace create from controller to SQL and back |
| Security | Distinguish authentication, CSRF, authority, and ownership | Predict when a request returns 401, 403, or 404 |
| Concurrency and queries | Handle stale edits and compose filtered pages | Reproduce a 409 and explain the unique sort tie-breaker |
| Optional integrations | Introduce one external service at a time | Run the relevant real-service test and explain its failure cases |

## Example catalog

1. Run the app with the default dev profile and inspect `/hello`, `/morning`, and the Thymeleaf homepage.
2. Study `ConfigurableController1A`, `ConfigurableController1B`, and `Controller1BConfig`: `/example`, `/example2`, `/hi` show property injection and structured configuration. These routes require login.
3. Explore `RestVerbController`: `/rest/piplup` returns JSON, `/piplup-xml` returns XML, `/piplup-teapot` demonstrates 418, `/rest/me` binds query parameters, and `/me-body` binds JSON.
4. Read the task feature for validated DTOs, service transactions, JPA specifications, ownership, pagination, optimistic locking, and Problem Details.
5. Inspect `PokemonClientService` for RestClient, timeouts, URI validation and Caffeine caching. `BookServiceClient` demonstrates a native HTTP service interface with a real DTO.
6. Enable PostgreSQL or MySQL and compare Flyway-managed schemas with H2. The MySQL profile uses the same entities and application with engine-specific migrations; `iac/` provides both database services. Run the database Testcontainers tests with the integration Maven profile.
7. Enable mongo and use `/mongo/notes` for document CRUD. This shared learning endpoint requires authentication but does not implement per-owner isolation.
8. Enable messaging and publish an ADMIN request to `/api/v1/messages` with `{"text":"Hello queue"}`. The listener receives the message from a durable queue. In session mode include CSRF.
9. Enable academic to compare progressively richer original note examples.

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=academic
```

| Example | Routes / classes |
| --- | --- |
| JPA basics | `/jpa/notes`, JpaNoteController/Service/Repository |
| Secured CRUD | `/secured/notes`, SecuredNoteController/Service |
| Ownership teaching | `/advanced/notes`, AdvancedNoteService, AdvancedAuthenticatedUser |
| JPA Thymeleaf UI | `/ui/notes`, JpaUIController, notes.html |
| Secured Thymeleaf UI | `/ui/secured-notes`, SecuredUIController, secured-notes.html |
| Manual access tokens | `/access`, `/benutzer`, AccessManager, DBAccessService |

Academic routes retain their original DTOs and learning-oriented response shapes. Some return entities, expose shared collections, or have simpler update/error semantics than the task API. The manual-token example keeps tokens in process and is layered behind normal Spring authentication. Use it to compare designs; use the task security model when starting application features.

Academic and production profiles cannot be combined. Demo data is local. JPA examples use independent tables. The original login password argument/order and inverted check were corrected, duplicate routes were separated, and test users are no longer inserted on every restart.

The adapted browser frontend is available at `/academic/notizen/demo.html` with the academic profile. Sign in with a Spring demo account, then register a separate exercise user in the page. Registration, login, user listing, update, logout and deletion use the root application's existing `/benutzer` and `/access` endpoints. The page sends CSRF tokens with writes and uses the current server origin, so no separate nginx server or CORS configuration is required. All frontend resources are absent when the academic profile is inactive.

The [exercise guide](EXERCISES.md) covers validation boundaries, title search, due-date migrations, testing layers, and integrations. Pick integrations to fit your application rather than enabling every profile by default.
