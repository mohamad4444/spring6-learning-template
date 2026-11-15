# Troubleshooting local learning

Run commands from the repository root. Start with `./mvnw --version` to see the Java runtime and Maven actually in use.

| Symptom | Likely cause | Next step |
| --- | --- | --- |
| Java version enforcement fails or `release version 21 not supported` | Maven is using an older JDK | Set `JAVA_HOME` to JDK 21 or newer and verify `./mvnw --version`; check the IDE's Maven JDK too |
| `./mvnw: Permission denied` | The executable bit was lost on checkout | On Unix run `chmod +x mvnw`, or use `bash mvnw`; Windows uses `mvnw.cmd` |
| Maven cannot resolve dependencies | The first build needs network access; a proxy or mirror may be unavailable | Check the first dependency error and Maven's proxy/mirror settings in `~/.m2/settings.xml` |
| `Port 8080 was already in use` | Another app or a previous run is listening | Stop that process or run `SERVER_PORT=8081 ./mvnw spring-boot:run`, then use port 8081 in your requests |
| Startup cannot resolve `app.database.url` | An explicit integration profile replaced default `dev` | Include `dev` explicitly, e.g. `-Dspring-boot.run.profiles=dev,mongo` |
| Database, MongoDB, or RabbitMQ connection refused | An enabled profile's service is stopped or points at the wrong port | Check `iac/README.md`, start that service, and compare its mapped port with the JVM settings |
| Compose starts successfully, but the local JVM has wrong credentials | Compose's `.env` is not exported to your shell | Export the matching variables or configure the IDE environment; see `CONFIGURATION.md` |
| Demo credentials do not work | The active profile does not seed demo accounts | For local learning use `dev` or `academic`; `test` and `prod` do not seed them |
| API GET returns 401 | Credentials are missing/incorrect or the app is in JWT mode | In session mode send the documented Basic credentials; in JWT mode send a valid bearer token |
| API write returns 403 | CSRF token/cookie is missing, stale, or mismatched; or the user lacks write authority | Fetch authenticated `/api/v1/csrf`, retain its cookie, send its named header, and verify `/api/v1/me` includes `WRITER` or `ADMIN` |
| Task returns 404 although it exists | The signed-in writer does not own it | Read it using the creating account; ADMIN can inspect all tasks |
| Update returns 409 | The supplied version is stale | GET the task, reconcile your changes, then PUT with the returned version |
| Academic or Mongo routes return 404 | Their runtime profile is inactive | Start with `academic` or `dev,mongo` respectively |
| Data disappears after restart | The dev/academic database is in memory | This is expected; enable PostgreSQL or MySQL for persistent data |
| Flyway reports a checksum mismatch | An applied migration was edited | Restore the applied file and add a new migration; use a fresh disposable database for experiments |
| Integration tests cannot find Docker | `-Pintegration` runs real services using Testcontainers | Start a supported Docker environment; the normal `./mvnw clean verify` needs no Docker |

`prod` deliberately rejects `dev`, `test`, and `academic`. `mysql` and `postgresql` are alternatives; `jwt` and `academic` are incompatible. Fix the selected profiles rather than removing `ProfileGuard`.

For a failed test, read the first failure in `target/surefire-reports` (normal tests) or `target/failsafe-reports` (container integration tests). Java 25 may print native-access or agent warnings; use the final Maven result and test reports to distinguish warnings from failures.

API responses include `X-Request-Id`; application log messages emitted during request handling include the same ID. Keep a failing response's status, body, and request ID together while debugging. Avoid copying passwords, session cookies, or bearer tokens into issue reports.

Return to [the first lesson](FIRST_STEPS.md), [configuration](CONFIGURATION.md), or [the learning catalog](LEARNING.md).
