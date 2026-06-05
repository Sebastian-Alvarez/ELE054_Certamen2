# Gestión de Películas — Documentación

API REST en memoria para administrar un catálogo de películas y sus comentarios.

## Stack

| Componente | Versión |
|---|---|
| Spring Boot | 4.0.5 |
| Java | 17 |
| Jakarta Validation | ✓ |
| Lombok | ✓ |
| Apache Commons Lang3 | 3.20.0 |

## Ejecución rápida

```bash
# Levantar la app
./mvnw spring-boot:run

# Ejecutar tests
./mvnw test

# Ejecutar test específico
./mvnw test -Dtest=PeliculasControllerTest
```

La API corre en **http://localhost:8094**.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/peliculas` | Lista todas las películas. Acepta `?q=` para filtrar por ID o título. |
| `POST` | `/peliculas` | Registra una película. Genera `tokenDescarga` automáticamente. |
| `GET` | `/peliculas/{id}` | Obtiene una película por ID (case-insensitive). |
| `GET` | `/peliculas/{id}/comentarios` | Retorna el array de comentarios de la película. |

## Arquitectura

Capas en el paquete `cl.usm.gestionPeliculasMemoria`:

```
PeliculasController
      │  @RestController — valida entrada con @Valid
      ▼
PeliculasService  (interface)
PeliculasServiceImpl  — genera tokenDescarga (RandomStringUtils.secure, 10 chars alfanuméricos)
      │
      ▼
PeliculasRepository  (interface)
PeliculasRepositoryImpl  — ArrayList<Pelicula> en memoria (singleton Spring)
```

**Comportamientos clave:**
- IDs comparados con `equalsIgnoreCase` — duplicados rechazados.
- `tokenDescarga` nunca viene del cliente, siempre se sobreescribe en el service.
- `comentarios` es `Comentario[]` embebido en `Pelicula`; no hay endpoint para agregar comentarios después de crear.
- Datos **no persisten** entre reinicios (almacenamiento en memoria).

## Modelo de datos

### Pelicula
```json
{
  "id": "PELI123",
  "titulo": "Inception",
  "director": "Christopher Nolan",
  "tokenDescarga": "a7F9kB2mN1",
  "comentarios": [
    { "usuario": "juan", "comentario": "Excelente." }
  ]
}
```

`id`, `titulo` y `director` son obligatorios (`@NotBlank`). `tokenDescarga` es de solo lectura.

## Navegación

- [API Reference (Swagger)](/api) — Documentación interactiva
- [Cobertura de Tests](/cobertura) — Reporte JaCoCo
