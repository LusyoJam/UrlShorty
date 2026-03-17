# UrlShorty

A lightweight URL shortener REST API built with Spring Boot. Submit a long URL, get an 8-character code back.
---

## Tech Stack

| Component | Technology |
|---|---|
| Framework | Spring Boot 4.0.3 |
| Language | Java 21 |
| Database | H2 (in-memory) |
| ORM | Spring Data JPA / Hibernate |
| Validation | Jakarta Bean Validation |
| Build Tool | Maven |

---

## Project Structure

```
src/main/java/com/urlshorty/urlshorty/
├── advice/        Global exception handler
├── controller/    HTTP endpoints
├── dto/           Request/response transfer objects
├── entity/        JPA entity
├── exception/     Custom exceptions
├── repository/    Spring Data JPA repository
└── service/       Business logic
```

---

## API Reference

### `POST /api/shorten`

Shortens a URL. Validates input, checks for duplicates, handles code collisions, and persists the mapping.

**Request Body**

```json
{
  "url": "https://www.example.com/some/very/long/path"
}
```

| Field | Type | Constraints |
|---|---|---|
| url | String | Required, minimum 50 characters |

**Responses**

| Status | Condition | Body |
|---|---|---|
| `200 OK` | URL already exists | Existing short code |
| `201 Created` | New URL registered | Generated short code (8 chars) |
| `400 Bad Request` | Validation failed | Error message |

---

### `GET /api/{code}`

Redirects to the original URL for a given short code.

**Example**

```
GET /api/a1b2c3d4
```

**Responses**

| Status | Condition | Behavior |
|---|---|---|
| `302 Found` | Code exists | Redirects to original URL |
| `404 Not Found` | Code not found | Error message |

---

## How It Works

**POST /shorten flow:**

1. Validate URL format. Throw `UrlNotValidException` if malformed.
2. Check if the URL already has a code. Return `200` with existing code if so.
3. Generate a new 8-character code from `UUID.randomUUID()`. Retry on collision.
4. Persist the mapping and return `201` with the new code.

---

## Configuration

Configured via `application.yml`:

| Property | Value |
|---|---|
| `datasource.url` | `jdbc:h2:mem:urlshortener` |
| `datasource.driver` | `org.h2.Driver` |
| `jpa.hibernate.ddl-auto` | `update` |
| `jpa.show-sql` | `true` |
| `h2.console.enabled` | `true` |
| `h2.console.path` | `/h2-console` |

The H2 console is available at `http://localhost:8080/h2-console` during development.
Use `jdbc:h2:mem:urlshortener` as the JDBC URL when connecting.

---

## Running Locally

**Prerequisites:** Java 21, Maven

```bash
# Clone the repository
git clone https://github.com/LusyoJam/UrlShorty.git
cd UrlShorty

# Run the application
./mvnw spring-boot:run
```

App starts at `http://localhost:8080`.

---

## Notes

- **H2 is in-memory** — all data is lost on restart. To persist, swap to PostgreSQL or MySQL by updating `application.yml` and the datasource dependency in `pom.xml`.
- **Short codes** are 8-character substrings of a randomly generated UUID. Collision probability is negligible but handled explicitly with a retry loop.
- **URL validation** uses both URI and URL parsing. Strings without a valid scheme (e.g. missing `https://`) will be rejected.
