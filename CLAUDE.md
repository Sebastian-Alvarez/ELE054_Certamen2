# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run app
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run single test class
./mvnw test -Dtest=GestionPeliculasMemoriaApplicationTests

# Build JAR
./mvnw package
```

App runs on **port 8094**.

## Architecture

Spring Boot 4.0.5 / Java 17 REST API for in-memory movie catalog. No database — storage is a plain `ArrayList<Pelicula>` inside a `@Repository` singleton bean (data lost on restart).

**Layer flow:** `PeliculasController` → `PeliculasService` → `PeliculasRepository`

Each layer has an interface + `Impl` class. Wire via `@Autowired` (field injection throughout).

### Key behaviors

- `tokenDescarga` is auto-generated on POST using `RandomStringUtils.secure().nextAlphanumeric(10)` — clients cannot set it.
- ID lookups are **case-insensitive** (`equalsIgnoreCase`). Duplicate IDs rejected at repository level.
- `GET /peliculas?q=` filters by partial match on `id` OR `titulo` (case-insensitive).
- `comentarios` is a `Comentario[]` array embedded inside `Pelicula` — no endpoint to add comments; they must be provided at creation time.

### Entities

- `Pelicula`: `id` (required), `titulo` (required), `director` (required), `tokenDescarga` (auto), `comentarios[]` (optional)
- `Comentario`: `usuario`, `comentario` — no validation constraints

All entities use Lombok (`@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor`), no `@Builder`.

### API surface (docs/openapi.yaml)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/peliculas` | List all; `?q=` to filter |
| POST | `/peliculas` | Create (generates tokenDescarga) |
| GET | `/peliculas/{id}` | Get by ID |
| GET | `/peliculas/{id}/comentarios` | Get comments for movie |

Current branch `feature/tests` is intended for adding tests. The only existing test is a context-load smoke test.
