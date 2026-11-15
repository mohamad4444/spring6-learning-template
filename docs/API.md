# API guide

The default security chain accepts form login, an authenticated session, or HTTP Basic. All writes require a valid CSRF token and its matching session cookie. Obtain a token with an authenticated `GET /api/v1/csrf`; the response includes `headerName` and `token`. A token obtained before form login may be rotated after login; fetch it again.

The `jwt` profile accepts bearer tokens only and does not require CSRF. It never accepts Basic credentials or session authentication. The identity provider issues tokens; the application verifies them.

## Main endpoints

| Method and path | Result |
| --- | --- |
| `GET /` | Thymeleaf landing page, public |
| `GET /hello`, `GET /morning` | Introductory text, public |
| `GET /api/v1/me` | Current username and authorities |
| `GET /api/v1/csrf` | Session CSRF token; absent in jwt profile |
| `POST /api/v1/tasks` | Create own task; WRITER or ADMIN; 201 with Location |
| `GET /api/v1/tasks` | Own tasks; ADMIN sees all |
| `GET /api/v1/tasks/{id}` | Own task or any task for ADMIN |
| `PUT /api/v1/tasks/{id}` | Update with current version; WRITER or ADMIN |
| `DELETE /api/v1/tasks/{id}` | Delete owned task; WRITER or ADMIN; 204 |
| `POST /api/v1/messages` | Sample RabbitMQ publish; ADMIN, messaging profile; 202 |
| `GET /pokemon/{name}` | Cached external Pokemon response |
| `GET /actuator/health` | Public summary |
| `GET /actuator/health/liveness`, `/actuator/health/readiness` | Public probes |
| `GET /actuator/prometheus` | Metrics, ADMIN |

Foreign task IDs return 404, including for writes. The task service checks ownership and roles inside the transactional boundary. Owners cannot be changed through request DTOs.

Create body:

```json
{"title":"Read the guide","description":"Work through the task example"}
```

Update body:

```json
{"title":"Read the guide","description":"Finished","status":"DONE","version":0}
```

Titles must be nonblank and at most 120 characters. Description is required, may be empty, and has a 2,000-character maximum. Status is `TODO`, `IN_PROGRESS`, or `DONE`. IDs are UUIDs. Version is required on updates and must equal the last response version. Reload after a 409 before retrying.

List example: `/api/v1/tasks?status=TODO&page=0&size=20&sort=createdAt,desc`. Page numbers start at zero; maximum size is 100. Supported sort fields: `id`, `title`, `status`, `createdAt`, `updatedAt`.

The default sort is `createdAt,desc`. The service appends `id,asc` as a unique tie-breaker unless the request already sorts by ID. Supply multiple `sort` parameters for multiple fields, for example `?sort=title,asc&sort=id,desc`. Ties have a predictable order; offset pages can still shift when matching tasks are inserted, updated, or deleted between requests.

The stable page envelope has `content`, `page`, `size`, `totalElements`, and `totalPages`.

## Errors

Responses use `application/problem+json` for API validation and domain errors. HTTP statuses are 400 for malformed/invalid requests, 401 for missing authentication, 403 for missing authority or CSRF, 404 for absent/inaccessible tasks, 409 for stale versions/data conflicts, and 502 for external HTTP service failures. JWT authentication errors use standard bearer authentication responses and WWW-Authenticate headers.

```json
{"type":"about:blank","title":"Bad Request","status":400,"detail":"Request validation failed","errors":[{"field":"title","message":"must not be blank"}]}
```

Responses carry `X-Request-Id`; safe incoming IDs are retained and unsafe values are replaced. Request IDs appear in application logs. Passwords are excluded from user JSON.

MongoDB and academic note endpoints are learning examples with their original response shapes; they do not provide the task API's ownership/concurrency contract. See [LEARNING.md](LEARNING.md).
