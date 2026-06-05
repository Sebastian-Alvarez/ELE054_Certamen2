# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run app
./mvnw spring-boot:run

# Run all tests + generate JaCoCo coverage report
./mvnw test

# Run single test class
./mvnw test -Dtest=PeliculasControllerTest

# Build JAR
./mvnw package
```

App runs on **port 8094**. JaCoCo report generates automatically to `target/site/jacoco/` on every `mvnw test`.

## Architecture

Spring Boot 4.0.5 / Java 17 REST API for in-memory movie catalog. No database — storage is a plain `ArrayList<Pelicula>` inside a `@Repository` singleton bean (data lost on restart).

**Layer flow:** `PeliculasController` → `PeliculasService` → `PeliculasRepository`

Each layer has an interface + `Impl` class. All use **constructor injection** (`@Autowired` on constructor) — required for unit tests which instantiate impls directly via `new XImpl(mockDep)`.

### Key behaviors

- `tokenDescarga` auto-generated on POST via `RandomStringUtils.secure().nextAlphanumeric(10)` — clients cannot set it.
- ID lookups are **case-insensitive** (`equalsIgnoreCase`). Duplicate IDs rejected at repository level.
- `GET /peliculas?q=` calls `service.filter(q)` — partial match on `id` OR `titulo` (case-insensitive).
- `comentarios` is a `Comentario[]` array embedded inside `Pelicula` — no endpoint to add comments post-creation.

### Entities

- `Pelicula`: `id` (required), `titulo` (required), `director` (required), `tokenDescarga` (auto), `comentarios[]` (optional)
- `Comentario`: `usuario`, `comentario` — no validation constraints

All entities use Lombok (`@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor`), no `@Builder`.

### API surface (`docs/openapi.yaml`)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/peliculas` | List all; `?q=` to filter |
| POST | `/peliculas` | Create (generates tokenDescarga) |
| GET | `/peliculas/{id}` | Get by ID |
| GET | `/peliculas/{id}/comentarios` | Get comments for movie |

## Tests

Coverage: **24 tests, 0 failures** across 4 test classes.

| Class | Strategy | Cases covered |
|---|---|---|
| `PeliculasControllerTest` | Mockito (`@ExtendWith(MockitoExtension.class)`) — mocks `PeliculasService` | getAll ok/ok-with-q/500/500-with-q, createPelicula ok/500, findById ok/404/500, getComentarios ok/404/500 |
| `PeliculasServiceImplTest` | Mockito — mocks `PeliculasRepositoryImpl` | createPelicula ok/nok, filter by id/titulo/no-match |
| `PeliculasRepositoryImplTest` | No mocks — real `PeliculasRepositoryImpl` | insert null-id/duplicate-id/ok, findById null/not-found/ok |
| `GestionPeliculasMemoriaApplicationTests` | `@SpringBootTest` — context load smoke test | contextLoads |

## Documentation (`docs/`)

Docsify site served from `docs/`. To preview locally: `npx docsify-cli serve docs` or `npx serve docs`.

| File | Purpose |
|---|---|
| `index.html` | Docsify app (vue theme, search, Java/YAML syntax highlight) |
| `README.md` | Home page — overview, endpoints, architecture |
| `_sidebar.md` | Navigation sidebar |
| `api.md` | Embeds `swagger.html` via iframe |
| `swagger.html` | Standalone Swagger UI 5 loading `openapi.yaml` |
| `cobertura.md` | Embeds `jacoco/index.html` via iframe |
| `jacoco/` | Full static JaCoCo report (copy from `target/site/jacoco/`) |
| `openapi.yaml` | OpenAPI 3.1.1 spec |

> `docs/jacoco/` is a static snapshot. After re-running tests, copy updated report: `cp -r target/site/jacoco docs/jacoco` (or PowerShell: `Copy-Item target\site\jacoco docs\jacoco -Recurse -Force`).
