# Running the college lessons

This application combines college lessons in one Spring Boot project. See the [README](README.md#spring-and-java-versions) for the exact Spring and Java versions and the [lesson index](README.md#college-lesson-index) for the topics and source files.

## Build and start

Use JDK 21 or newer and the included Maven wrapper (`mvnw.cmd` on Windows):

```bash
./mvnw clean verify
./mvnw spring-boot:run
```

Open <http://localhost:8080>. The default `dev` profile uses an in-memory H2 database. The [README](README.md#start) lists the demo accounts.

For the JPA notes, secured notes, manual-token and Benutzer browser lessons:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=academic
```

Open <http://localhost:8080/academic/notizen/demo.html> and sign in with a demo account. Follow the [learning guide](docs/LEARNING.md) for the other lesson routes.

## Packaged application and configuration

```bash
./mvnw package
java -jar target/springtemplate.jar --spring.profiles.active=academic
```

To change the configuration lesson's message, start the JAR with a property override:

```bash
java -jar target/springtemplate.jar --controller.message="Hello environment"
```

Sign in and visit `/example` or `/hi` to see the message. See the [configuration reference](docs/CONFIGURATION.md) for profiles and environment variables.

## Optional services and further help

Use the repository's [Docker Compose setup](iac/README.md) for PostgreSQL, MySQL, MongoDB and RabbitMQ. Those lessons require the corresponding service and Spring profile; the basic and academic H2 lessons run without Docker.

- [First task API walkthrough](docs/FIRST_STEPS.md)
- [Practice exercises](docs/EXERCISES.md)
- [Troubleshooting](docs/TROUBLESHOOTING.md)
- [API requests](examples/api.http)
