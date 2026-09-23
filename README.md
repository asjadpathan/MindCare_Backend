# MindCare Backend

Spring Boot backend for the MindCare platform.

## Tech Stack
- Java 21
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- MySQL (primary)
- Maven

## Prerequisites
- JDK 21
- Maven (or use `./mvnw`)
- MySQL running locally (default config)

## Configuration
Set these environment variables before running the app:

| Variable | Required | Description |
|---|---|---|
| `JWT_SECRET` | Yes | JWT signing key (minimum 32 characters). |
| `DB_URL` | No | Database URL. Default: `jdbc:mysql://localhost:3306/mindcare` |
| `DB_USERNAME` | No | Database username. Default: `root` |
| `DB_PASSWORD` | Yes | Database password |
| `SPRING_AI_OPENAI_API_KEY` | Yes (for AI endpoints) | API key for Groq/OpenAI compatible endpoint |
| `MAIL_USERNAME` | Yes (for mail features) | SMTP username/email |
| `MAIL_PASSWORD` | Yes (for mail features) | SMTP app password |

## Run Locally
```bash
./mvnw spring-boot:run
```

Application runs on:
- `http://localhost:5051`

## Run Tests
```bash
./mvnw test
```

## Security Note
- Sensitive values are intentionally externalized via environment variables.
- Do not commit real secrets in source code or property files.
