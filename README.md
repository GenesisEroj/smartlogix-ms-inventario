# SmartLogix - Microservicio de Inventario (MS Inventario)

## Descripción
Microservicio encargado de la gestión de inventario y stock de productos
en la plataforma SmartLogix. Implementa los patrones **Repository** y **CQRS**
para garantizar una arquitectura escalable y mantenible.

## Tecnologías
- Java 17
- Spring Boot 3.5.15
- Spring Data JPA
- MySQL
- Lombok
- Maven

## Patrones de Diseño Implementados
- **Repository Pattern**: Abstracción de la capa de acceso a datos
- **CQRS**: Separación de operaciones de lectura y escritura
- **DTO**: Transferencia de datos entre capas

## Estructura del Proyecto
src/
├── main/
│   ├── java/com/smartlogix/inventario/
│   │   ├── controller/    # InventarioController
│   │   ├── service/       # InventarioService
│   │   ├── repository/    # ProductoRepository
│   │   ├── model/         # Producto
│   │   └── dto/           # ProductoDTO
│   └── resources/
│       └── application.properties
└── test/
└── java/com/smartlogix/inventario/
└── MsInventarioApplicationTests
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

## Endpoints disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/inventario | Agregar producto |
| GET | /api/inventario | Obtener todos los productos |
| GET | /api/inventario/{productoId} | Obtener producto por ID |
| GET | /api/inventario/categoria/{categoria} | Obtener por categoría |
| GET | /api/inventario/stock-bajo/{limite} | Obtener productos con stock bajo |
| PUT | /api/inventario/{productoId}/stock | Actualizar stock |
| DELETE | /api/inventario/{id} | Eliminar producto |

## Pruebas Unitarias
```bash
mvn test
```
Las pruebas cubren el servicio con Mockito, verificando:
- Agregar productos
- Consulta de productos
- Actualización de stock
- Manejo de excepciones

## Equipo
- Genesis Eroj
- Francisco Monsalve

**DSY1106 - Desarrollo Fullstack III**

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

## Endpoints disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/inventario | Agregar producto |
| GET | /api/inventario | Obtener todos los productos |
| GET | /api/inventario/{productoId} | Obtener producto por ID |
| GET | /api/inventario/categoria/{categoria} | Obtener por categoría |
| GET | /api/inventario/stock-bajo/{limite} | Obtener productos con stock bajo |
| PUT | /api/inventario/{productoId}/stock | Actualizar stock |
| DELETE | /api/inventario/{id} | Eliminar producto |

## Pruebas Unitarias
```bash
mvn test
```
Las pruebas cubren el servicio con Mockito, verificando:
- Agregar productos
- Consulta de productos
- Actualización de stock
- Manejo de excepciones

## Equipo
- Genesis Eroj
- Francisco Monsalve

**DSY1106 - Desarrollo Fullstack III**