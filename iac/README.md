# Infrastructure

Run these commands from the repository root. Docker Compose v2 is required.

| File | Purpose |
| --- | --- |
| `Dockerfile` | Maven build stage and non-root Java 21 runtime |
| `Dockerfile.dockerignore` | Build exclusions for this Dockerfile |
| `compose.yaml` | Local PostgreSQL, optional MySQL/MongoDB/RabbitMQ, and application |
| `.env.example` | Local environment example; copy to `iac/.env` |

The Compose build context is the repository root (`..` relative to this directory), and its Dockerfile is `iac/Dockerfile` relative to that context. The Dockerfile-specific ignore file excludes credentials, Git/IDE state, build output and documentation. These paths follow the [Compose build specification](https://docs.docker.com/reference/compose-file/build/) and [Docker build-context rules](https://docs.docker.com/build/concepts/context/).

## Configure

```bash
cp iac/.env.example iac/.env
```

Make targets load `iac/.env` if it exists and otherwise use the Compose defaults. Direct Compose commands below pass the file explicitly. Compose environment values do not automatically enter a JVM started from Maven or an IDE; export any changed credentials for those processes.

## PostgreSQL

```bash
make up
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev,postgresql
```

To run the application in a container as well:

```bash
make docker
```

The app service connects to PostgreSQL by its Compose hostname and waits for a healthy database.

## MySQL

```bash
make mysql-up
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev,mysql
```

MySQL 8.4 has a persistent volume, a dedicated application user, and a health check. The `mysql` Spring profile selects the driver's connection defaults and the MySQL Flyway schema. The bundled app service uses PostgreSQL; use the JVM command above for the MySQL local setup.

## MongoDB and RabbitMQ

```bash
docker compose --env-file iac/.env -f iac/compose.yaml --profile mongo --profile messaging up -d
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev,postgresql,mongo,messaging
```

For all services in containers, set `SPRING_PROFILES_ACTIVE=dev,postgresql,mongo,messaging` in `iac/.env`, then run:

```bash
docker compose --env-file iac/.env -f iac/compose.yaml --profile app --profile mongo --profile messaging up --build -d
```

## Validate, build and stop

```bash
make docker-config
make image
make down
```

Equivalent build command: `docker build -f iac/Dockerfile -t springtemplate:local .`.

`make down` stops services across all profiles and preserves named volumes. Deliberately resetting local data requires `docker compose --env-file iac/.env -f iac/compose.yaml --profile '*' down -v`; this removes the database and broker data.

All host ports bind to loopback. This is a local development setup. Configure your platform's secrets, HTTPS, resource limits, storage and backups for production; see [deployment](../docs/DEPLOYMENT.md).
