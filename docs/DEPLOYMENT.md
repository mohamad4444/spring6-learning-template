# Deployment and operations

## Build and run

```bash
./mvnw clean verify
java -jar target/springtemplate.jar
```

Deploy the executable JAR with JDK 21+ or build the included image with `docker build -f iac/Dockerfile -t springtemplate:local .`. CI verifies tests before building the image; the Docker build skips re-running tests. The runtime image runs as a non-root user. It needs writable temporary storage at `/tmp`.

Docker and Compose configuration lives in `iac/`; [the infrastructure guide](../iac/README.md) contains all commands.

For production, inject `SPRING_PROFILES_ACTIVE=prod`, `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` from your platform's secret store. For MySQL, activate `prod,mysql` and supply a MySQL JDBC URL; this selects its Flyway migrations. The prod profile disables Swagger/OpenAPI, requires DB settings, enables secure session cookies, and uses Flyway plus Hibernate validation. Do not use the local Compose file as a complete production deployment.

Use HTTPS and a trusted reverse proxy. Because forwarded headers are enabled in production, prevent direct untrusted access to the application port and have the proxy replace forwarded headers. Set resource limits, persistent database storage, backups, and a graceful termination window exceeding the 20-second application shutdown timeout.

For session deployments, configure a long, unique administrator password through the one-time bootstrap settings. For bearer API deployments use `prod,jwt`, `JWT_ISSUER_URI`, and `JWT_AUDIENCE`; provision users and roles in the identity provider. Basic credentials must only be sent over HTTPS. Session mode currently stores sessions in process, so multiple replicas require sticky sessions or an explicit shared-session implementation.

## Database lifecycle

Use a new database for the initial Flyway migration. Applied migrations are immutable. Add versioned SQL for later changes and test against PostgreSQL and MySQL with `./mvnw -Pintegration verify`. Coordinate migrations across replicas with your rollout process. Schema changes that remove columns need an expand/contract rollout; restoring an old JAR does not reverse a migration.

`make down` preserves named volumes. The explicit reset command is in [iac/README.md](../iac/README.md); use it only when deliberately removing disposable data.

## Health and metrics

`/actuator/health/liveness` checks process liveness; `/actuator/health/readiness` checks Spring's readiness state. These probe groups do not automatically include every dependency. Extend readiness groups for dependencies that must gate traffic; avoid putting remote dependencies into liveness.

The public health endpoint returns a summary. Detailed health and `/actuator/prometheus` require ADMIN. Set up authenticated collection through your platform or provide a dedicated monitoring security chain appropriate for your infrastructure. Only health, info, and Prometheus are exposed; arbitrary actuator endpoints are not enabled.

Logs include request IDs. Incoming `X-Request-Id` must contain at most 64 safe characters. Forward logs to your platform rather than writing into the container filesystem. The sample Rabbit consumer logs message length rather than message contents.

## Troubleshooting

| Symptom | Check |
| --- | --- |
| Build fails on Java version | `java -version`, `JAVA_HOME`; use JDK 21+ |
| Port 8080 is occupied | Set `SERVER_PORT` for a JVM or `APP_PORT` for Compose |
| 401 | Credentials, active security profile, JWT issuer/audience/time claims |
| 403 on a session write | Matching session cookie and CSRF header; WRITER/ADMIN authority |
| Foreign task gives 404 | Intended owner isolation |
| 409 on PUT | Fetch the task again and use its latest version |
| Datasource missing when adding profiles | Include dev locally or configure postgresql/mysql/prod |
| Flyway checksum/schema validation fails | Restore applied migrations; create a new migration |
| Mongo/Rabbit endpoints absent | Activate matching Spring profiles and infrastructure |
| Testcontainers cannot start | Docker daemon and container permissions; integration tests require Docker |
| Database credentials changed in iac/.env | Existing volume retains original DB credentials; change them inside DB or deliberately recreate disposable volume |
