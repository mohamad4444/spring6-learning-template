# Contributing

Use Java 21 or newer and the Maven wrapper. Keep changes focused and include a behavioral test for security or persistence changes.

1. Create a branch and implement the change.
2. Run `./mvnw clean verify`.
3. For persistence/integration changes, run `./mvnw -Pintegration verify` with Docker available.
4. Add a new Flyway migration instead of editing an applied migration.
5. Update the README, configuration reference, and HTTP examples when behavior changes.

Never commit `.env`, credentials, database volumes, generated builds, or IDE state. Keep academic examples behind their profile. Describe the changed behavior and verification results in pull requests.

Infrastructure changes belong in `iac/`. Update Make targets, CI and the infrastructure guide when paths or commands change.
