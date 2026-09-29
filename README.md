# Distributed Task Management System

This project is a Spring Boot-based REST API for managing users and tasks. It exposes a backend service for registering users, creating and assigning tasks, updating status, filtering task records, and persisting data in PostgreSQL.

The application is built with Java 17, Spring Boot 3, Spring Data JPA, Hibernate, Flyway, and Spring Security. It includes validation, centralized exception handling, pagination, and API-key authentication.

---

## Overview

The current implementation supports:

- User creation and retrieval
- Task creation, retrieval, update, and deletion
- Task status updates with a PATCH endpoint
- Task filtering and pagination
- Assignment of tasks to a specific user
- PostgreSQL persistence with Flyway migration scripts
- Standardized JSON error responses
- API-key protection for incoming requests

---

## Technology Stack

- Java 17
- Spring Boot 3.2.1
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Security
- PostgreSQL
- Flyway
- Maven
- Lombok

---

## Database Setup

Create the PostgreSQL database used by the application:

```sql
CREATE DATABASE task_management;
```

The application configuration in `src/main/resources/application.yml` expects PostgreSQL to be available locally on port 5432 using the `task_management` database.

Example configuration:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/task_management
    username: postgres
    password: your_password

  flyway:
    baselineOnMigrate: true
    locations: classpath:db/migration

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

---

## Authentication

All API requests must include the following header:

```http
X-API-KEY: Aditya@2003
```

If the API key is missing or invalid, the server responds with `401 Unauthorized`.

---

## Running the Application

From the project root, run:

```bash
mvn clean install
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

---

## API Endpoints

### User Endpoints

#### Create a user

```http
POST /api/users
```

Request body:

```json
{
  "name": "Aditya A.V",
  "email": "aditya@example.com"
}
```

Behavior:
- Returns `201 Created` when the user is created
- Returns `409 Conflict` if the email already exists

#### Get all users

```http
GET /api/users?page=0&size=10
```

Behavior:
- Returns a paginated list of users
- Supports Spring Data `Pageable` parameters

#### Get a user by ID

```http
GET /api/users/{id}
```

Behavior:
- Returns the user matching the ID
- Returns `404 Not Found` if the user does not exist

---

### Task Endpoints

#### Create a task

```http
POST /api/tasks
```

Request body:

```json
{
  "title": "Complete task management",
  "description": "Task management backend implementation",
  "status": "TODO",
  "priority": "MEDIUM",
  "dueDate": "2026-01-30",
  "assignedToUserId": 1
}
```

Supported values:
- Status: `TODO`, `IN_PROGRESS`, `DONE`
- Priority: `LOW`, `MEDIUM`, `HIGH`

Behavior:
- Creates a task and stores it in PostgreSQL
- Returns `200 OK` in the current controller implementation
- Returns `404 Not Found` if the assigned user does not exist
- Returns `400 Bad Request` for invalid enum values

#### Get all tasks

```http
GET /api/tasks?page=0&size=10&status=TODO&priority=HIGH&userId=1
```

Supported query parameters:
- `status` – filter by task status
- `priority` – filter by task priority
- `userId` – filter tasks by assigned user
- `page` – page number, default `0`
- `size` – page size, default `10`

Behavior:
- Returns a paginated and optionally filtered list of tasks
- Returns `200 OK`

#### Get a task by ID

```http
GET /api/tasks/{id}
```

Behavior:
- Returns the matching task
- Returns `404 Not Found` if the task is missing

#### Update task status

```http
PATCH /api/tasks/{id}/status?status=IN_PROGRESS
```

Behavior:
- Updates only the task status field
- Returns the updated task with `200 OK`

#### Delete a task

```http
DELETE /api/tasks/{id}
```

Behavior:
- Deletes the task by ID
- Returns no content in the current implementation

#### Update a task

```http
PUT /api/tasks/{id}
```

Request body:

```json
{
  "title": "Updated Task Title",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "dueDate": "2026-02-10",
  "assignedToUserId": 1
}
```

Behavior:
- Updates the existing task when the ID exists
- Creates a new task when the ID is not found
- Returns `200 OK` for update and `201 Created` for creation in the current implementation

---

## Validation and Error Handling

The project validates request payloads and handles common failure cases with centralized exception handling.

Examples of handled cases:
- Missing or invalid user email
- Missing or invalid task title
- Invalid status and priority values
- Duplicate email addresses
- Missing user or task records

These errors are returned as JSON responses with appropriate status codes such as:
- `400 Bad Request`
- `401 Unauthorized`
- `404 Not Found`
- `409 Conflict`

---

## Author

**Aditya K**
Backend Developer | Java | Spring Boot
