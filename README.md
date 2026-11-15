# Spring Boot 3.5 Learning Template

A combined collection of lessons and exercises from college, kept in one runnable Spring application. It covers the progression from a first HTTP controller to configuration, REST, databases, security, browser clients, and integrations. The task API brings several of these topics together in a worked example.

## Spring and Java versions

| Component | Version used |
| --- | --- |
| Spring Boot | **3.5.16**, pinned by the parent in [pom.xml](pom.xml) |
| Spring Framework | **6.2.19**, managed by Spring Boot |
| Spring Security | **6.5.11**, managed by Spring Boot |
| Java | **21** compilation target; JDK 21 or newer required |
| Maven | **3.9.9** through the included wrapper |

Spring Boot and Spring Framework have separate version numbers: this is a Spring Boot **3.5** project using Spring Framework **6.2**. Maven's Boot parent manages the Spring dependency versions together. After changing that parent, check the resolved versions with:

```bash
./mvnw dependency:tree '-Dincludes=org.springframework.boot:spring-boot,org.springframework:spring-core,org.springframework.security:spring-security-core'
```

## College lesson index

The order below is a suggested study sequence. Use `dev` for the basic lessons and `academic` for the original note, Benutzer, and browser exercises. Both run with H2 without Docker.

| Lesson | What to study | Starting point |
| --- | --- | --- |
| First controllers | Routing and text responses | [HelloWorldController](src/main/java/com/github/mohamad4444/springtemplate/controller/HelloWorldController.java), `/hello`, `/morning` |
| Configuration and injection | `@Value`, constructor injection, `@ConfigurationProperties` | [ConfigurableController1A](src/main/java/com/github/mohamad4444/springtemplate/controller/ConfigurableController1A.java), [Controller1BConfig](src/main/java/com/github/mohamad4444/springtemplate/controller/Controller1BConfig.java), `/example`, `/example2`, `/hi` |
| REST and DTOs | HTTP methods, query/body binding, JSON/XML, status codes | [RestVerbController](src/main/java/com/github/mohamad4444/springtemplate/controller/RestVerbController.java), `/rest/piplup`, `/rest/me` |
| HTTP clients | External API calls, error handling, timeouts, caching | [PokemonClientService](src/main/java/com/github/mohamad4444/springtemplate/service/PokemonClientService.java), [BookServiceClient](src/main/java/com/github/mohamad4444/springtemplate/client/BookServiceClient.java) |
| JPA persistence | Entities, repositories, services and CRUD | [JpaNoteController](src/main/java/com/github/mohamad4444/springtemplate/controller/JpaNoteController.java), `/jpa/notes` (`academic`) |
| SQL databases | H2, PostgreSQL, MySQL and Flyway migrations | [Configuration guide](docs/CONFIGURATION.md), `src/main/resources/db/` |
| Authentication and authorization | Passwords, Spring Security, CSRF and note ownership | [SecuredNoteController](src/main/java/com/github/mohamad4444/springtemplate/controller/SecuredNoteController.java), [AdvancedNoteService](src/main/java/com/github/mohamad4444/springtemplate/service/AdvancedNoteService.java) (`academic`) |
| Server-rendered pages | Spring MVC models and Thymeleaf | [JpaUIController](src/main/java/com/github/mohamad4444/springtemplate/controller/JpaUIController.java), `/ui/notes`, `/ui/secured-notes` (`academic`) |
| Browser and manual tokens | Benutzer registration/login, JavaScript requests and sessions | [Browser exercise](src/main/resources/academic/notizen/demo.html), `/academic/notizen/demo.html` (`academic`) |
| Document databases | MongoDB documents, repositories and CRUD | [MongoNoteController](src/main/java/com/github/mohamad4444/springtemplate/controller/MongoNoteController.java), `/mongo/notes` (`dev,mongo`) |
| Messaging | RabbitMQ publishing, queues and consumers | [MessageController](src/main/java/com/github/mohamad4444/springtemplate/messaging/MessageController.java), `/api/v1/messages` (`dev,messaging`) |
| Putting it together | Validation, ownership, pagination, concurrency, errors and tests | [Task API walkthrough](docs/FIRST_STEPS.md), `/api/v1/tasks` |

See the [learning guide](docs/LEARNING.md) for the full route catalog, optional JWT authentication, and exercises. Most lesson routes require login; session writes also require CSRF. MongoDB, RabbitMQ and external HTTP lessons need their corresponding services.

## Start

Install JDK 21 or newer. The Maven wrapper downloads Maven on its first run.

```bash
./mvnw clean verify
./mvnw spring-boot:run
```

Windows: use `mvnw.cmd`. Open [the application](http://localhost:8080), [Swagger UI](http://localhost:8080/swagger-ui.html), or [health](http://localhost:8080/actuator/health).

The default `dev` profile uses disposable H2 data and these local accounts:

| Email | Password | Authorities |
| --- | --- | --- |
| `ester.nutzer@hs-kl.de` | `user1-secret` | WRITER |
| `anders.nutzer@hs-kl.de` | `user2-secret` | WRITER |
| `armin.nistrator@hs-kl.de` | `admin-secret` | ADMIN, WRITER |

Demo accounts are created only in `dev` or `academic`. Production requires external database settings and real account provisioning.

New to Spring? Follow [your first task API lesson](docs/FIRST_STEPS.md) to create a task, trace the request through the code, and reproduce validation, ownership, and concurrency errors. Continue with [small practice exercises](docs/EXERCISES.md), or use [troubleshooting](docs/TROUBLESHOOTING.md) if setup fails.

## Included

| Area | Implementation |
| --- | --- |
| REST | Versioned task CRUD, validated DTOs, creation locations and 204 deletes |
| Queries | Owner scope, status filtering, bounded pagination and sort allowlist |
| Data | JPA/Hibernate, H2, PostgreSQL, MySQL, Flyway migrations and indexes |
| Concurrency | Versioned updates with 409 responses for stale writes |
| Security | Encoded passwords, session/form and Basic login, CSRF, service authority and ownership checks |
| JWT | Optional OAuth2 resource server with issuer, audience and role validation |
| Errors | Problem Details and field validation errors |
| Integrations | MongoDB notes; RabbitMQ publisher, topology and consumer |
| HTTP clients | RestClient with timeouts, native book HTTP interface and Caffeine caching |
| UI | Thymeleaf pages and a same-origin Benutzer browser exercise |
| Operations | Health probes, protected Prometheus metrics, request IDs and graceful shutdown |
| Delivery | Executable JAR, non-root Docker image, Compose, CI and Dependabot |
| Tests | API/security/profile tests, MySQL dialect compatibility, and opt-in real database/broker tests |

## Repository layout

| Path | Purpose |
| --- | --- |
| `src/main/java/` | Application features and academic examples |
| `src/main/resources/` | Profiles, Flyway migrations, templates and browser assets |
| `src/test/java/` | Automated application and integration tests |
| `iac/` | Dockerfile, Compose, build exclusions and environment example |
| `docs/` | Architecture, configuration, API, deployment and learning guides |
| `examples/api.http` | Requests for an HTTP client |
| `scripts/smoke.py` | Repeatable live API verification |

## Use the API

Start the server, then run the live smoke check:

```bash
python3 scripts/smoke.py
```

Session writes require a CSRF token and its matching session cookie. Obtain them with authenticated `GET /api/v1/csrf`; send the returned token in its named header. [examples/api.http](examples/api.http) and [the API guide](docs/API.md) cover creation, updates, pagination and deletion. The `jwt` profile accepts bearer tokens without session CSRF.

## Databases and containers

All Docker commands run from the repository root. The Make targets use `iac/compose.yaml` and load `iac/.env` when present.

```bash
cp iac/.env.example iac/.env
make up                         # PostgreSQL
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev,postgresql

# Or build/start the app and PostgreSQL together:
make docker
```

MySQL is an alternative database for the same application and schema:

```bash
make mysql-up
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev,mysql
```

Include `academic` before the database profile to use the Benutzer/Notiz examples with that database, for example `academic,mysql`. Choose one SQL database profile at a time. Each engine has its own Flyway migration location; use a fresh database for first setup.

Compose reads `iac/.env`; a directly launched JVM requires exported variables or IDE environment settings. Default example credentials match the local profiles. Full commands and optional MongoDB/RabbitMQ services are documented in [iac/README.md](iac/README.md).

## Browser and learning examples

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=academic
```

Sign in at [the browser exercise](http://localhost:8080/academic/notizen/demo.html) with a Spring demo account, then create a separate exercise user in the page. Registration, login, listing, update, logout and deletion use the existing Benutzer/token backend with session cookies and CSRF protection. The exercise's logout leaves the Spring login active.

The academic profile also enables the JPA note APIs and Thymeleaf note views. Read [the learning guide](docs/LEARNING.md) for the complete route catalog.

## Verify and package

```bash
./mvnw clean verify             # No Docker required
./mvnw -Pintegration verify     # Docker: PostgreSQL, MySQL, MongoDB and RabbitMQ
./mvnw package
java -jar target/springtemplate.jar
make docker-config             # Requires Docker Compose
make image                     # Requires Docker
```

Coverage is generated at `target/site/jacoco/index.html`. CI runs Java 21/25 tests, container integration tests, Compose validation and the Docker build.

## Customize

Rename the Maven coordinates, package, application and Compose project name. Follow `task/` for new features, keep validation at HTTP boundaries and authorization inside services, and add Flyway migrations for schema changes. Enable integrations to fit your application and configure accounts, HTTPS, secrets, backups and monitoring for deployment.

Read [architecture](docs/ARCHITECTURE.md), [configuration](docs/CONFIGURATION.md), [the API guide](docs/API.md), [deployment](docs/DEPLOYMENT.md), [contributing](CONTRIBUTING.md) and [security](scripts/SECURITY.md).
