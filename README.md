# MindCare Backend

A production-style Spring Boot backend for a mental wellness platform that combines user management, mood tracking, medication workflows, community discovery, doctor booking, and AI-powered assistance.

This repository highlights backend engineering skills across API design, authentication, persistence, service-layer architecture, and secure configuration management.

---

## Why this project stands out

- **Real product domain**: mental wellness use cases with multiple connected features.
- **Layered backend architecture**: controllers → services → repositories → entities.
- **Security-first updates**: JWT secret and external credentials are now environment-driven (no hardcoded secrets).
- **Integration breadth**: database, email workflows, and AI chat integration.

---

## Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.x
- **Security**: Spring Security + JWT
- **Data Access**: Spring Data JPA (MySQL/H2)
- **AI Integration**: Spring AI (OpenAI-compatible/Groq endpoint)
- **Build Tool**: Maven Wrapper (`mvnw`)

---

## Core Features

### 1) Authentication & User Context
- User signup and login
- JWT-based stateless authentication
- Authenticated user profile retrieval (`/api/user/me`)

### 2) Mood Tracking
- Create mood entries
- Retrieve mood history
- Mood summary endpoint for lightweight analytics

### 3) Medication Management
- Create and list medications
- Mark medication as taken
- Deactivate medication records

### 4) Community Discovery
- Browse communities
- Join/leave communities
- View joined and suggested communities
- Search communities and fetch community details

### 5) Doctor Booking
- Browse doctor list
- Create bookings
- View bookings
- Cancel bookings

### 6) AI-powered Assistance
- AI chat endpoint for interactive responses
- Daily tip streaming endpoint (SSE) for personalized wellness guidance

---

## API Surface (High-level)

| Domain | Base Path |
|---|---|
| Auth | `/api/auth` |
| User | `/api/user` |
| Mood | `/api` |
| Medications | `/api/medications` |
| Communities | `/api/communities` |
| Doctors/Bookings | `/api/doctors` |
| AI Chat | `/api/ai-chat` |
| Daily Tip Stream | `/api/daily-tip` |

---

## Project Structure

```text
src/main/java/com/MindCare
├── config        # Security, JWT utilities, request filtering
├── controller    # REST API endpoints
├── service       # Business logic and integrations
├── repository    # Spring Data repositories
├── entity        # JPA domain models
└── dto           # Request/response DTOs
```

---

## Secure Configuration

All sensitive values are externalized via environment variables.

### Required / Common Variables

| Variable | Required | Purpose |
|---|---|---|
| `JWT_SECRET` | Yes | JWT signing secret (minimum 32 characters) |
| `DB_URL` | No | JDBC URL (default points to local MySQL) |
| `DB_USERNAME` | No | DB username (default: `root`) |
| `DB_PASSWORD` | Yes | DB password |
| `SPRING_AI_OPENAI_API_KEY` | Yes (for AI endpoints) | API key for AI model provider |
| `MAIL_USERNAME` | Yes (for email features) | SMTP login email |
| `MAIL_PASSWORD` | Yes (for email features) | SMTP app password |

> Never commit real credentials into source code or property files.

---

## Local Setup

### 1. Prerequisites
- JDK 21
- MySQL (if using default DB config)
- Internet access for Maven dependency resolution

### 2. Configure environment variables
Set the variables above in your shell/IDE run configuration.

### 3. Run the application

```bash
./mvnw spring-boot:run
```

Default server port: **5051**

### 4. Run tests

```bash
./mvnw test
```

No license file is currently included in this repository.
