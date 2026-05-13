# SmartLogix - Microservicio de Inventario (MS Inventario)

## Descripción

Microservicio encargado de la gestión de inventario y stock de productos en la plataforma SmartLogix. Implementa los patrones **Repository** y **CQRS** para garantizar una arquitectura escalable y mantenible.

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 3.5.15-SNAPSHOT |
| Spring Data JPA | — |
| Spring Boot DevTools | — |
| MySQL Connector/J | — |
| Lombok | — |
| Maven | 3.9+ |

## Patrones de Diseño Implementados

- **Repository Pattern**: Abstracción de la capa de acceso a datos mediante `ProductoRepository` (Spring Data JPA).
- **CQRS**: Separación explícita de comandos (`agregarProducto`, `actualizarStock`, `eliminarProducto`) y consultas (`obtenerTodos`, `obtenerPorProductoId`, `obtenerPorCategoria`, `obtenerStockBajo`) en `InventarioService`.
- **DTO**: `ProductoDTO` para transferencia de datos entre capas sin exponer la entidad directamente.
- **Builder**: Lombok `@Builder` aplicado en `Producto` y `ProductoDTO`.

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/smartlogix/inventario/
│   │   ├── MsInventarioApplication.java   # Punto de entrada
│   │   ├── controller/
│   │   │   └── InventarioController.java  # REST endpoints
│   │   ├── service/
│   │   │   └── InventarioService.java     # Lógica de negocio (CQRS)
│   │   ├── repository/
│   │   │   └── ProductoRepository.java    # Acceso a datos (JPA)
│   │   ├── model/
│   │   │   └── Producto.java              # Entidad JPA (@PrePersist / @PreUpdate)
│   │   └── dto/
│   │       └── ProductoDTO.java           # Objeto de transferencia de datos
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/smartlogix/inventario/
        └── MsInventarioApplicationTests.java  # Pruebas unitarias con Mockito
```

## Modelo de Datos

### Entidad `Producto`

| Campo | Tipo | Restricciones |
|---|---|---|
| `id` | `Long` | PK, auto-generado |
| `productoId` | `String` | NOT NULL, UNIQUE |
| `nombre` | `String` | NOT NULL |
| `stock` | `Integer` | NOT NULL |
| `precio` | `Double` | NOT NULL |
| `categoria` | `String` | NOT NULL |
| `fechaActualizacion` | `LocalDateTime` | Gestionado por `@PrePersist` / `@PreUpdate` |

## Requisitos

- Java 17+
- Maven 3.9+
- MySQL 8.0+

## Configuración

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smartlogix_inventario
spring.datasource.username=root
spring.datasource.password=root
server.port=8082
```

## Instalación y Ejecución

```bash
# Clonar el repositorio
git clone https://github.com/GenesisEroj/smartlogix-ms-inventario

# Entrar al directorio
cd smartlogix-ms-inventario

# Ejecutar con Maven
mvn spring-boot:run

# O con Maven Wrapper
./mvnw spring-boot:run
```

## Endpoints Disponibles

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/inventario` | Agregar un nuevo producto |
| `GET` | `/api/inventario` | Obtener todos los productos |
| `GET` | `/api/inventario/{productoId}` | Obtener producto por `productoId` |
| `GET` | `/api/inventario/categoria/{categoria}` | Obtener productos por categoría |
| `GET` | `/api/inventario/stock-bajo/{limite}` | Obtener productos con stock < límite |
| `PUT` | `/api/inventario/{productoId}/stock?cantidad={n}` | Sumar `n` unidades al stock |
| `DELETE` | `/api/inventario/{id}` | Eliminar producto por `id` (PK) |

> **Nota:** `PUT /stock` recibe `cantidad` como query parameter y la **suma** al stock existente (no reemplaza).

## Pruebas Unitarias

```bash
mvn test
```

Las pruebas están implementadas con **JUnit 5** y **Mockito** (`@ExtendWith(MockitoExtension.class)`), cubriendo `InventarioService` de forma aislada:

| Test | Descripción |
|---|---|
| `agregarProducto_debeRetornarProductoGuardado` | Verifica que el producto se guarda y retorna correctamente |
| `obtenerTodos_debeRetornarListaDeProductos` | Verifica que se retorna la lista completa |
| `obtenerPorProductoId_debeRetornarProducto` | Verifica búsqueda por `productoId` |
| `obtenerPorProductoId_debeArrojarExcepcionSiNoExiste` | Verifica lanzamiento de `RuntimeException` si no existe |
| `actualizarStock_debeActualizarStockDelProducto` | Verifica que el stock se incrementa correctamente |

## Equipo

- Genesis Eroj
- Francisco Monsalve

**DSY1106 - Desarrollo Fullstack III**