# Your first task API lesson

Allow about 30 minutes. You need JDK 21 or newer, a terminal, curl, and Python 3 for reading JSON. The commands below use Bash; on Windows use Git Bash and `./mvnw.cmd`. You can also send requests from [examples/api.http](../examples/api.http).

By the end, you will have created a task, followed a request through the code, and observed how validation, authentication, ownership, and optimistic locking change HTTP responses. No Docker or external services are needed.

## 1. Start and check the application

From the repository root:

```bash
./mvnw clean verify
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Leave the server running. In a second Bash terminal:

```bash
base=http://localhost:8080
curl --silent --show-error "$base/actuator/health"
curl --include --silent --show-error "$base/api/v1/tasks"
```

Expect health status `UP`, then HTTP **401** for the task list. The application is running; the API requires authentication. H2 stores this lesson's data in memory, so restarting the server resets it.

## 2. Authenticate and obtain a CSRF token

Keep this second terminal open for the remaining steps; it holds the lesson's variables and cookies.

```bash
credentials=ester.nutzer@hs-kl.de:user1-secret
work_dir=$(mktemp -d)
trap 'rm -rf "$work_dir"' EXIT

# Retain the session cookie between requests, including error responses.
api() {
    curl --silent --show-error --user "$credentials" \
        --cookie "$work_dir/cookies" --cookie-jar "$work_dir/cookies" \
        --header 'Accept: application/json' "$@"
}

# Read one top-level field from a JSON response on standard input.
field() {
    python3 -c 'import json,sys; print(json.load(sys.stdin)[sys.argv[1]])' "$1"
}

api --fail "$base/api/v1/me"
api --fail "$base/api/v1/csrf" > "$work_dir/csrf.json"
csrf_header=$(field headerName < "$work_dir/csrf.json")
csrf_token=$(field token < "$work_dir/csrf.json")
```

Expect your email and the `WRITER` authority from `/me`. Basic credentials authenticate each request; Spring still requires CSRF for writes in this security mode. The CSRF token belongs to the session represented by the saved cookie. Fetch it again after a server restart or form login.

Read [AdvancedWebSecurityConfig](../src/main/java/com/github/mohamad4444/springtemplate/security/AdvancedWebSecurityConfig.java) and [CsrfController](../src/main/java/com/github/mohamad4444/springtemplate/web/CsrfController.java). Authentication answers who you are; CSRF checks whether a write carries the expected session token.

## 3. Create a task and trace the request

```bash
api --fail --dump-header "$work_dir/create.headers" \
    --output "$work_dir/task.json" \
    --header "$csrf_header: $csrf_token" \
    --header 'Content-Type: application/json' \
    --data '{"title":"Learn Spring","description":"Trace my first request"}' \
    "$base/api/v1/tasks"

cat "$work_dir/create.headers"
python3 -m json.tool "$work_dir/task.json"
task_id=$(field id < "$work_dir/task.json")
version=$(field version < "$work_dir/task.json")
```

Expect HTTP **201**, a `Location` header ending in the task's UUID, status `TODO`, version `0`, and your email as owner.

Follow these files in order, or place debugger breakpoints in `create` and `saveAndFlush`:

| Step | Source | What to notice |
| --- | --- | --- |
| Bind and validate JSON | [TaskController](../src/main/java/com/github/mohamad4444/springtemplate/task/TaskController.java), [TaskDtos](../src/main/java/com/github/mohamad4444/springtemplate/task/TaskDtos.java) | `@Valid` checks the input record before calling the service |
| Authorize and save | [TaskService](../src/main/java/com/github/mohamad4444/springtemplate/task/TaskService.java) | `@PreAuthorize` checks authority; the principal supplies the owner; `@Transactional` wraps the write |
| Persist | [Task](../src/main/java/com/github/mohamad4444/springtemplate/task/Task.java), [TaskRepository](../src/main/java/com/github/mohamad4444/springtemplate/task/TaskRepository.java) | JPA supplies the UUID, timestamps, and version; Spring Data supplies the repository implementation |
| Return HTTP | [TaskController](../src/main/java/com/github/mohamad4444/springtemplate/task/TaskController.java) | An explicit response DTO and `ResponseEntity.created` define the public contract |
| Create the schema | [V1 migration](../src/main/resources/db/migration/V1__initial_schema.sql) | Flyway creates the table at startup; Hibernate validates the mapping |

Checkpoint: explain why `owner` is absent from the create DTO, and why a persistence entity is not returned directly.

## 4. Observe validation, CSRF, and ownership failures

A blank title reaches Bean Validation and returns **400**, with `errors` identifying `title`:

```bash
api --include --header "$csrf_header: $csrf_token" \
    --header 'Content-Type: application/json' \
    --data '{"title":" ","description":"Invalid example"}' \
    "$base/api/v1/tasks"
```

Omitting CSRF returns **403** before the controller runs:

```bash
api --include --header 'Content-Type: application/json' \
    --data '{"title":"Missing CSRF","description":"Will not be created"}' \
    "$base/api/v1/tasks"
```

The second demo writer cannot read your task and receives **404**:

```bash
curl --include --silent --show-error \
    --user 'anders.nutzer@hs-kl.de:user2-secret' \
    --header 'Accept: application/json' "$base/api/v1/tasks/$task_id"
```

Read `ownedTask` in `TaskService` and [ApiExceptionHandler](../src/main/java/com/github/mohamad4444/springtemplate/web/ApiExceptionHandler.java). A foreign ID and a missing ID have the same response so the API does not reveal another user's tasks. List queries apply ownership in SQL, including the total count.

## 5. Update and reproduce a stale write

Build an update using the version you originally read:

```bash
cat > "$work_dir/update.json" <<JSON
{"title":"Learn Spring","description":"Traced and tested","status":"DONE","version":$version}
JSON

api --include --request PUT --header "$csrf_header: $csrf_token" \
    --header 'Content-Type: application/json' \
    --data-binary "@$work_dir/update.json" "$base/api/v1/tasks/$task_id"
```

Expect **200** with status `DONE` and version `1`. Run the same `api` command again with the same file: expect **409**. This models two browser tabs editing the same version. Read the task again to get its current version before making another edit:

```bash
api --fail "$base/api/v1/tasks/$task_id"
api --fail "$base/api/v1/tasks?status=DONE&page=0&size=1&sort=title,asc"
```

The list response has `content`, `page`, `size`, `totalElements`, and `totalPages`. Its totals include your matching tasks only. The service appends an ID sort to break ties between equal titles or timestamps. Offset pages can still shift if tasks are added or changed between requests.

There are two concurrency checks: the service compares the client's version with the loaded row, and Hibernate's `@Version` detects a database write that races after the row was loaded. `saveAndFlush` makes the updated version available before the response DTO is built.

## 6. Delete and connect the lesson to tests

```bash
api --include --request DELETE --header "$csrf_header: $csrf_token" \
    "$base/api/v1/tasks/$task_id"
api --include "$base/api/v1/tasks/$task_id"
```

Expect **204** with no body, then **404**. Remove the local lesson files and exit this terminal when finished:

```bash
rm -rf "$work_dir"
trap - EXIT
```

From the repository root, run the corresponding automated tests:

```bash
./mvnw -Dtest=TaskApiTests test
python3 scripts/smoke.py
```

The smoke check requires the running dev server and creates/deletes its own temporary task. [TaskApiTests](../src/test/java/com/github/mohamad4444/springtemplate/TaskApiTests.java) starts a separate test application and disposable H2 database. MockMvc tests use `user(...)` and `csrf()` to exercise authorization and HTTP behavior; the live smoke check uses real credentials and cookies.

Next: choose a small change from [the exercises](EXERCISES.md), then explore optional features through [the learning catalog](LEARNING.md). For setup problems, use [troubleshooting](TROUBLESHOOTING.md).
