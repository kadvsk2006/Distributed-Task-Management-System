# 🗂️ Task Management REST API

A **Spring Boot--based Task Management REST API** that allows users to
create, manage, update, and track tasks with secure API access using
**API Key authentication**.

---

## 🚀 Features

- User Management (Create, View, List)
- Task Management (Create, Read, Update, Delete)
- Task Status Update using PATCH
- Pagination & Filtering
- PostgreSQL Database
- API Key Security
- Global Exception Handling

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3
- Spring Data JPA (Hibernate)
- Spring Security (API Key)
- PostgreSQL
- Lombok
- Maven

---

## 🔐 API Authentication

All API requests must include the following header:

    X-API-KEY: Mahesh@2003

If the API key is missing or invalid, the server returns **401
Unauthorized**.

---

## 🗄️ Database Setup

Create database:

```sql
CREATE DATABASE task_management;
```

## 2️⃣ Database Tables

**👤 Users Table**

```sql
CREATE TABLE public.users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE
);

```

**📝 Tasks Table**

```sql
CREATE TABLE task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    status VARCHAR(20),
    priority VARCHAR(20),
    due_date DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    assigned_to BIGINT REFERENCES users(id)
);

```

`application.yml` configuration:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/task_management
    username: postgres
    password: your_password

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

---

## ▶️ Run Application

```bash
mvn clean install
mvn spring-boot:run
```

Application runs at:

    http://localhost:8080

---

## 📌 API Endpoints Documentation

For **detailed API documentation**, **request/response examples**, and **Postman screenshots**,  
please refer to the following document:

📄 **Task Management REST API.pdf**

This PDF includes:

- Complete API flow explanation
- Request & response screenshots from Postman
- Validation and error handling examples
- Pagination and filtering details

### 👤 User APIs

#### 1️⃣ Create User

**POST** `/api/users`

Request Body:

```json
{
  "name": "Mahesh A.V",
  "email": "mahesh@example.com"
}
```

Response: - `201 Created` - `409 Conflict` (Email already exists)

---

#### 2️⃣ Get All Users (Pagination)

**GET** `/api/users?page=0&size=10`

Response: - `200 OK`

---

#### 3️⃣ Get User by ID

**GET** `/api/users/{id}`

Response: - `200 OK` - `404 Not Found`

---

### 📝 Task APIs

#### 4️⃣ Create Task

**POST** `/api/tasks`

Request Body:

```json
{
  "title": "Complete task management",
  "description": "Task Management system by sentra world",
  "status": "TODO",
  "priority": "MEDIUM",
  "dueDate": "2026-01-30",
  "assignedToUserId": 1
}
```

Response: - `201 Created` - `404 Not Found` (User not found)

---

#### 5️⃣ Get All Tasks (Filters + Pagination)

**GET** `/api/tasks?page=0&size=10&status=TODO&priority=HIGH`

Optional Filters: - status - priority - assignedToUserId

Response: - `200 OK`

---

#### 6️⃣ Get Task by ID

**GET** `/api/tasks/{id}`

Response: - `200 OK` - `404 Not Found`

---

#### 7️⃣ Update Task (Full Update)

**PUT** `/api/tasks/{id}`

Request Body:

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

Response: - `200 OK` - `404 Not Found`

---

#### 8️⃣ Update Task Status (Partial Update)

**PATCH** `/api/tasks/{id}/status?status=DONE`

Response: - `200 OK` - `404 Not Found`

---

#### 9️⃣ Delete Task

**DELETE** `/api/tasks/{id}`

Response: - `200 OK No Content` - `404 Not Found`

---

## ⚠️ Error Handling

Standard error response format:

```json
{
  "message": "Resource not found",
  "status": 404,
  "timestamp": "2026-01-10T01:28:43"
}
```

---

## 👨‍💻 Author

**Mahesh A V**\
Backend Developer \| Java \| Spring Boot
