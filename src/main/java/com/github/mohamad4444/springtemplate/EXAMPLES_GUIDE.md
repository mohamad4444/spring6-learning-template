# Spring Boot Examples Guide

This guide provides a comprehensive overview of all Spring Boot examples included in this template project. Each example demonstrates key Spring Boot principles and patterns used in real-world applications.

---

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Basic Examples](#basic-examples)
4. [REST API Examples](#rest-api-examples)
5. [Data Persistence Examples](#data-persistence-examples)
6. [External API Integration](#external-api-integration)
7. [Security Examples](#security-examples)
8. [Configuration Examples](#configuration-examples)
9. [Testing the Examples](#testing-the-examples)

---

## Overview

This template contains **85+ Java files** demonstrating Spring Boot best practices across multiple domains:

- **REST Controllers**: Building RESTful APIs with various HTTP methods
- **Data Persistence**: MongoDB (NoSQL) and PostgreSQL (SQL) integration
- **Security**: Authentication and authorization patterns
- **External APIs**: Consuming third-party REST APIs
- **Configuration**: Property management and profiles
- **UI Integration**: Thymeleaf templates for server-side rendering

All examples are **commented and functional**, ready to be used as reference or starting points for your own projects.

---

## Prerequisites

### Required Services

Different examples require different external services:

| Example | Required Services | Port |
|---------|------------------|------|
| **data1** | MongoDB | 27017 |
| **data2** | PostgreSQL | 5433 |
| **data2Security1/2** | PostgreSQL | 5433 |
| **poke** | Internet access | - |
| **rest, config, intro** | None | - |

### Starting External Services

**MongoDB (for data1 example):**
```bash
docker run -d -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=root \
  -e MONGO_INITDB_ROOT_PASSWORD=local \
  mongo:latest
```

**PostgreSQL (for data2 and security examples):**
```bash
docker run -d -p 5433:5432 \
  -e POSTGRES_PASSWORD=local \
  postgres:latest
```

---

## Basic Examples

### 1. Hello World Controller

**Location**: `HelloWorldController.java`, `GoodMorningController.java`

**Spring Boot Principles**:
- `@RestController`: Combines `@Controller` and `@ResponseBody`
- `@GetMapping`: Maps HTTP GET requests to handler methods
- Component scanning: Auto-discovered by Spring Boot

**Test**:
```bash
# Start the application
./mvnw spring-boot:run

# Test endpoints
curl http://localhost:8080/hello     # Returns: "Hello World!"
curl http://localhost:8080/morning   # Returns: "Good Morning!"
```

**Key Concepts**:
- Spring Boot auto-configures embedded Tomcat server
- Controllers are automatically discovered in the application package
- Simple annotation-based routing

---

## REST API Examples

### 2. REST Verb Examples

**Location**: `rest/controller/VerbExampleController.java`

**Spring Boot Principles**:
- HTTP method mapping (`@GetMapping`, `@PostMapping`)
- Content negotiation (JSON, XML)
- `ResponseEntity` for custom HTTP responses
- `@RequestParam` and `@RequestBody` for input handling

**Test**:
```bash
# GET request returning JSON
curl http://localhost:8080/piplup
# Response: {"name":"Piplup","age":8,"favoriteThings":["Fish","Swimming"]}

# GET request returning XML
curl http://localhost:8080/piplup-xml
# Response: <ExampleResponse><name>Piplup</name>...</ExampleResponse>

# GET with custom HTTP status (418 I'm a teapot)
curl -v http://localhost:8080/piplup-teapot

# POST with request parameters
curl -X POST "http://localhost:8080/me?name=John&age=25&favorites=Java&favorites=Spring"

# POST with JSON body
curl -X POST http://localhost:8080/me-body \
  -H "Content-Type: application/json" \
  -d '{"name":"John","age":25,"favoriteThings":["Java","Spring"]}'
```

**Key Concepts**:
- Content negotiation via `produces` parameter
- Using `ResponseEntity` to control HTTP status codes
- Request parameter binding with `@RequestParam`
- JSON request body binding with `@RequestBody`
- Java Records for DTOs (Data Transfer Objects)

---

## Data Persistence Examples

### 3. MongoDB Example (data1)

**Location**: `data1/`

**Spring Boot Principles**:
- `@Document`: MongoDB entity annotation
- `MongoRepository`: Spring Data repository interface
- `@Service`: Service layer annotation
- CRUD operations with NoSQL

**Structure**:
```
data1/
├── controller/
│   ├── NoteController.java         # REST endpoints
│   ├── NotePostRequestBody.java    # DTO for creating notes
│   └── NotePutRequestBody.java     # DTO for updating notes
├── repository/
│   ├── MongoNoteRepository.java    # MongoDB repository
│   └── NoteDocument.java           # MongoDB entity
├── service/
│   └── NoteService.java            # Business logic
└── Data1Application.java           # Example app (commented)
```

**Configuration Required**:
```properties
spring.data.mongodb.uri=mongodb://root:local@localhost:27017/data?authSource=admin&authMechanism=SCRAM-SHA-1
```

**Test** (requires MongoDB running):
```bash
# Create a note
curl -X POST http://localhost:8080/notes \
  -H "Content-Type: application/json" \
  -d '{"title":"My Note","content":"Note content"}'

# Get all notes
curl http://localhost:8080/notes

# Update a note
curl -X PUT http://localhost:8080/notes/{id} \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated Title","content":"Updated content"}'

# Delete a note
curl -X DELETE http://localhost:8080/notes/{id}
```

**Key Concepts**:
- Spring Data MongoDB auto-implements repository methods
- No need to write SQL/queries for basic CRUD
- Document-based storage model
- MongoDB connection string configuration

---

### 4. PostgreSQL/JPA Example (data2)

**Location**: `data2/`

**Spring Boot Principles**:
- `@Entity`: JPA entity annotation
- `JpaRepository`: Spring Data JPA repository
- `@Transactional`: Transaction management
- Thymeleaf integration for server-side rendering

**Structure**:
```
data2/
├── controller/
│   ├── NoteController.java         # REST API endpoints
│   ├── UIController.java           # Thymeleaf UI endpoints
│   ├── NotePostRequestBody.java    # DTO for creating
│   ├── NotePutRequestBody.java     # DTO for updating
│   └── NoteCreate.java             # Form DTO
├── repository/
│   ├── NoteRepository.java         # JPA repository
│   └── NoteEntity.java             # JPA entity
├── service/
│   └── NoteService.java            # Business logic
└── Data2Application.java           # Example app (commented)
```

**Configuration Required**:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/postgres
spring.datasource.username=postgres
spring.datasource.password=local
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.mvc.hiddenmethod.filter.enabled=true
```

**Test** (requires PostgreSQL running):
```bash
# REST API - same as MongoDB example
curl http://localhost:8080/notes

# UI - open in browser
# http://localhost:8080/ui/notes
```

**Key Concepts**:
- JPA annotations (`@Entity`, `@Id`, `@GeneratedValue`)
- Automatic table creation via Hibernate
- ACID transactions
- Integration with Thymeleaf for HTML rendering
- Hidden method filter for PUT/DELETE from HTML forms

---

## External API Integration

### 5. Pokemon API Client Example

**Location**: `poke/`

**Spring Boot Principles**:
- `RestTemplate`: HTTP client for consuming REST APIs
- `@Value`: Injecting configuration properties
- Response POJOs for JSON deserialization
- External API integration patterns

**Structure**:
```
poke/
├── controller/
│   └── PokeController.java         # Exposes Pokemon data
├── service/
│   ├── PokeClientService.java      # Calls external API
│   └── response/
│       ├── PokeApiV2PokemonResponse.java
│       ├── PokeApiV2MoveResponse.java
│       ├── PokeApiV2TypeResponse.java
│       └── PokeApiV2NamedApiResponse.java
```

**Configuration Required**:
```properties
pokeapi.pokemon.url=https://pokeapi.co/api/v2/pokemon
```

**Test** (requires internet access):
```bash
# Get Pokemon data
curl http://localhost:8080/pokemon/pikachu
curl http://localhost:8080/pokemon/charizard
```

**Key Concepts**:
- Using `RestTemplate` to consume external REST APIs
- Property injection with `@Value`
- Automatic JSON to Java object mapping
- Error handling for external API calls

---

## Security Examples

### 6. Basic Security (data2Security1)

**Location**: `data2Security1/`

**Spring Boot Principles**:
- Spring Security auto-configuration
- In-memory authentication
- HTTP Basic authentication
- Securing REST endpoints

**Test**:
```bash
# Without credentials - returns 401 Unauthorized
curl http://localhost:8080/secure/notes

# With credentials
curl -u username:password http://localhost:8080/secure/notes
```

**Key Concepts**:
- `@EnableWebSecurity`: Enables Spring Security
- SecurityFilterChain configuration
- Basic authentication
- Password encoding

---

### 7. Advanced Security (data2Security2)

**Location**: `data2Security2/`

**Spring Boot Principles**:
- Custom `UserDetailsService`
- User entity with JPA
- Form-based login
- Session management
- `@AuthenticationPrincipal`: Access authenticated user

**Structure**:
```
data2Security2/
├── controller/
│   └── NoteController.java         # Secured endpoints
├── security/
│   ├── AppUserDetailsService.java  # Custom UserDetailsService
│   ├── AuthenticatedUser.java      # User principal
│   ├── UserEntity.java             # User JPA entity
│   ├── UserRepository.java         # User repository
│   ├── UserService.java            # User management
│   ├── WebSecurityConfig.java      # Security configuration
│   └── TestUserInit.java           # Demo users initialization
├── repository/
│   └── NoteRepository.java
└── service/
    └── NoteService.java            # User-scoped notes
```

**Key Concepts**:
- Database-backed user authentication
- Custom UserDetailsService implementation
- Password hashing with BCrypt
- User-owned resources (notes belong to users)
- Programmatic access to authenticated user

---

## Configuration Examples

### 8. Configuration Properties

**Location**: `config/`

**Spring Boot Principles**:
- `@Value`: Simple property injection
- `@ConfigurationProperties`: Type-safe configuration
- Configuration beans
- Spring profiles

**Structure**:
```
config/
├── controller/
│   ├── ConfigurableController1A.java    # Uses @Value
│   ├── ConfigurableController1B.java    # Uses @ConfigurationProperties
│   └── Controller1BConfig.java          # Configuration class
```

**Configuration in `application.properties`**:
```properties
# Simple @Value injection
controller.message=Hello World
controller.othermessage=Hello HS-KL
controller.log=Responding to a call of /example

# Environment variable with default
controller.message=${CONTROLLER_MESSAGE:Hello World}

# Profiles
spring.profiles.active=dev
```

**Test**:
```bash
# Test @Value configuration
curl http://localhost:8080/example

# Test @ConfigurationProperties
curl http://localhost:8080/1b
```

**Key Concepts**:
- Property injection from `application.properties`
- Environment variable support with defaults
- Type-safe configuration with `@ConfigurationProperties`
- Profile-specific configurations

---

## Testing the Examples

### Running the Application

```bash
# Clean and compile
./mvnw clean compile

# Run the application
./mvnw spring-boot:run

# Run on specific port
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=prod
```

### Quick Test Suite

```bash
# Basic endpoints (no dependencies)
curl http://localhost:8080/hello
curl http://localhost:8080/morning

# REST examples
curl http://localhost:8080/piplup
curl http://localhost:8080/piplup-xml

# Pokemon API (requires internet)
curl http://localhost:8080/pokemon/pikachu

# Data examples (require MongoDB/PostgreSQL)
curl http://localhost:8080/notes
```

---

## Spring Boot Principles Summary

This template demonstrates the following Spring Boot core concepts:

| Principle | Examples |
|-----------|----------|
| **Auto-configuration** | All examples use Spring Boot's auto-configuration |
| **Component Scanning** | Controllers automatically discovered |
| **Dependency Injection** | Services injected into controllers |
| **Spring Data** | MongoDB and JPA repositories |
| **REST Controllers** | REST API examples |
| **Security** | Spring Security integration |
| **Configuration** | Properties and profiles |
| **External APIs** | RestTemplate usage |
| **Templates** | Thymeleaf integration |

---

## Next Steps

1. **Explore the code**: Open each example package and read the JavaDoc comments
2. **Run examples**: Start with basic ones, then move to those requiring external services
3. **Modify and experiment**: Change code to understand how Spring Boot works
4. **Build your own**: Use these examples as templates for your projects

---

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)
- [Spring Security](https://spring.io/projects/spring-security)
- [Thymeleaf](https://www.thymeleaf.org/)

---

**Last Updated**: February 2026  
**Spring Boot Version**: 3.4.3  
**Java Version**: 21
