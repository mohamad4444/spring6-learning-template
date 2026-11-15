# Practice with small, verifiable changes

Complete [the first lesson](FIRST_STEPS.md) before these exercises. Each exercise describes a result to achieve and evidence to check. Work on one at a time in a branch; use a fresh dev database when practicing schema changes.

## 1. Add a validation rule

Reject task descriptions longer than 500 characters. Apply the same rule to creation and updates in `TaskDtos`.

Acceptance checks:

- A description of exactly 500 characters succeeds.
- A description of 501 characters returns 400 with an `errors` entry for `description`.
- An invalid update leaves the stored task and its version unchanged.
- An empty description still succeeds.

Extend `TaskApiTests` with boundary cases. Explain why this change does not require reducing the database column's capacity.

## 2. Add title search

Add optional `q` to `GET /api/v1/tasks` for case-insensitive title substring search. Define blank input as no filter and treat `%` and `_` as literal characters. Keep status, ownership, pagination, and sorting composable.

Start with `TaskController.list` and `TaskService.list`. Build a JPA `Specification` instead of loading all tasks and filtering in memory.

Acceptance checks:

- Searching `spring` matches `Learn Spring` and `SPRING practice`.
- Search combines with `status=DONE` and reports the correct filtered total.
- A writer never sees another writer's matches; an ADMIN can see both.
- `%` and `_` in a search do not accidentally match every title.
- Tied titles retain predictable page boundaries.

Add HTTP examples and document the query contract in `API.md`.

## 3. Add optional due dates

Add a nullable `LocalDate dueDate` to tasks and their create, update, and response DTOs. Decide that PUT fully replaces this optional value: omitting it clears the due date. Keep existing clients working when they do not send it.

Add `V2__task_due_date.sql` to **both** Flyway migration directories; leave V1 unchanged. See the existing migrations for engine conventions.

Acceptance checks:

- Existing rows migrate with a null due date.
- Create, read, and update preserve an ISO date such as `2027-01-15`.
- Omitting the date in PUT clears it; a malformed date returns 400.
- Foreign owners cannot change the date, and stale updates still return 409.
- `./mvnw clean verify` passes; with Docker, `./mvnw -Pintegration verify` validates the schema on real engines.

For the migration check, start a disposable SQL database with V1, create a row, then restart with V2 and verify that row. A clean database test alone does not demonstrate an upgrade with existing data.

## 4. Choose a testing boundary

Add a `@DataJpaTest` for the title search specification or a new repository query from your own feature. Make the test prove matching, exclusion, and pagination totals. Use Flyway to initialize the schema.

Compare that test with `TaskApiTests` and `PostgresIT`:

| Test | Question it answers |
| --- | --- |
| Repository slice | Does the query return the correct rows? |
| MockMvc application test | Do routing, validation, authorization, transactions, and JSON work together? |
| Real database integration | Do migrations and mappings work on the target database engine? |
| Live smoke check | Can a running application authenticate and complete a task lifecycle? |

A repository test does not exercise method security. Keep ownership and authority tests at the application boundary as well.

## 5. Explore one integration

Pick the topic that fits your next project. Use [configuration](CONFIGURATION.md) and [the infrastructure guide](../iac/README.md) to start only the services you need.

| Topic | Change to attempt | Evidence to collect |
| --- | --- | --- |
| HTTP clients | Test an upstream 500 and a malformed response | A mock server test and a 502 API response |
| Caching | Request the same Pokemon twice | One upstream call for two application calls; explain cache expiry |
| Messaging | Make a consumer idempotent using a message ID | Replaying a message does not repeat the business effect |
| JWT | Connect a development identity provider | Correct issuer/audience/role works; wrong audience is rejected |

Before calling an exercise complete, explain the request path, demonstrate its failure cases, and update the API examples. Coverage numbers alone do not establish correct behavior.
