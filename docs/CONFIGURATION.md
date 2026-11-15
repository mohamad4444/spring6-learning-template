# Configuration reference

Default profile is `dev` only when no active profiles are supplied. When adding profiles, include `dev` explicitly to keep demo accounts. Profiles are runtime Spring profiles; `integration` is a Maven build profile.

| Profile | Purpose |
| --- | --- |
| `dev` | In-memory H2; local demo users; Thymeleaf hot reload |
| `test` | H2 with no seeded accounts |
| `postgresql` | PostgreSQL connection from environment; combine with dev locally |
| `mysql` | MySQL connection defaults and MySQL Flyway migrations |
| `prod` | Required external DB settings, secure session cookie, no demo users or API docs |
| `academic` | Original JPA/security/manual-token/UI exercises, own H2 database and demo users |
| `mongo` | Mongo auto-configuration, repository, notes service and routes |
| `messaging` | RabbitMQ topology, publisher endpoint and listener |
| `jwt` | Stateless bearer authentication, required issuer and audience |

`prod` rejects `dev`, `test`, and `academic`. `jwt` rejects `academic` because the exercises use local account principals. Optional integrations can be combined with production. In production, use `prod,mongo,messaging,jwt` as needed; `prod` uses the database engine selected by `DB_URL`. Add `mysql` when using MySQL so its migration location is selected. The mysql and postgresql profiles are mutually exclusive.

| Variable | Default / requirement |
| --- | --- |
| `SERVER_PORT` | 8080 for a JVM |
| `DB_URL` | PostgreSQL default: `jdbc:postgresql://localhost:5432/springtemplate`; MySQL default: `jdbc:mysql://localhost:3306/springtemplate`; required in prod |
| `DB_USERNAME` | postgresql profile: `springtemplate`; required in prod |
| `DB_PASSWORD` | postgresql profile: local example password; required in prod |
| `MONGODB_URI` | `mongodb://localhost:27017/springtemplate` in mongo profile |
| `RABBITMQ_HOST`, `RABBITMQ_PORT` | localhost, 5672 |
| `RABBITMQ_USERNAME`, `RABBITMQ_PASSWORD` | Local examples; override outside development |
| `POKEMON_API_URL` | `https://pokeapi.co/api/v2/pokemon` |
| `BOOKS_API_URL` | `http://localhost:8082`; client example, no automatic request at startup |
| `JWT_ISSUER_URI`, `JWT_AUDIENCE` | Required by jwt profile |
| `APP_BOOTSTRAP_ENABLED` | false unless explicitly enabled |
| `BOOTSTRAP_ADMIN_EMAIL`, `BOOTSTRAP_ADMIN_PASSWORD` | Required when bootstrap is enabled; minimum password length 16 |
| `CONTROLLER_MESSAGE` | Hello World |
| `SMOKE_USERNAME`, `SMOKE_PASSWORD` | Override credentials used by the Python smoke script |

Compose-only port variables and database/broker examples are in `iac/.env.example`. Copy it to `iac/.env`; Make targets load that file when present. For direct Compose calls use `--env-file iac/.env -f iac/compose.yaml`. A local JVM uses exported variables or the IDE environment.

Database profiles set `app.database.*` defaults. Shared datasource properties resolve `DB_URL`, `DB_USERNAME` and `DB_PASSWORD` first. Production requires those variables directly, so adding a database profile cannot replace the production requirement with development defaults.

## Authentication choices

Session mode is the default. Production accounts can be bootstrapped once with `APP_BOOTSTRAP_ENABLED=true` and the bootstrap variables, then the setting and password should be removed from the runtime environment. Bootstrap only creates a missing email; it does not reset existing passwords. Account enrollment and password recovery are application-specific features.

For JWT, configure a real OAuth2/OpenID Connect identity provider and start with `prod,jwt`. Spring verifies signature, issuer, audience and token time constraints. The `roles` claim must be an array such as `["WRITER"]` or `["ADMIN","WRITER"]`. Only those two roles become application authorities. The subject claim identifies the owner. Use a single trusted issuer; changing authentication modes/subjects does not migrate existing task ownership automatically.

Flyway owns schema changes and Hibernate validates mappings. H2/PostgreSQL use `classpath:db/migration`; MySQL uses `classpath:db/mysql`. The MySQL variant uses binary UUIDs, auto-increment IDs, native enums and UTC date-time storage for the same entities. The MySQL profile sets Hibernate's JDBC time zone to UTC. Do not switch migration locations for an existing database. Existing databases without Flyway history require a deliberate migration plan. The template does not automatically baseline existing data or use create-drop/update.

The HTTP client uses a 3-second connect timeout and 5-second read timeout. Pokemon responses use a local Caffeine cache capped at 500 entries with 10-minute expiry. This cache is per process; it does not synchronize across replicas.
