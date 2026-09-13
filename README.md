# Authentication Service

A Spring Boot REST API providing user registration and authentication using Spring Security, JWT, PostgreSQL, and BCrypt password hashing.

## Scope and Implementation Notes

The Sawiya project specifically required authentication and validation for the following scenarios:

- Email – invalid format, missing/empty email, duplicate email
- Password – minimum length, required characters, empty/missing password

Although user registration was not explicitly specified as a separate feature, a registration endpoint was implemented to provide the appropriate context for validating these requirements, particularly duplicate email validation.

The implemented authentication flow therefore includes:

- User registration with email and password validation
- Duplicate email detection
- User login using email and password
- JWT generation after successful authentication
- Appropriate HTTP status codes for validation, authentication, and conflict errors

Registration was kept separate from login so that account creation and authentication responsibilities remain clearly defined.

## Features

- User login
- User registration
- JWT-based authentication
- Password hashing using BCrypt
- Request validation
- Duplicate email validation
- Authentication error handling
- Global exception handling
- Stateless Spring Security configuration
- Controller and API validation tests

## Technologies

- Java 17
- Spring Boot 3.4.3
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- Jakarta Bean Validation
- Lombok
- Maven
- JUnit 5
- Mockito
- MockMvc

## Project Structure

```text
src
├── main
│   └── java
│       └── com.sawiya.authservice
│           ├── controller
│           │   └── AuthController
│           ├── service
│           │   ├── AuthService
│           │   └── UserService
│           ├── repository
│           │   └── UserRepository
│           ├── model
│           │   └── User
│           ├── dto
│           │   ├── LoginRequestDTO
│           │   ├── LoginResponseDTO
│           │   ├── RegisterRequestDTO
                ├── RegisterResponseDTO
│           │   └── ErrorResponseDTO
│           ├── security
│           │   └── SecurityConfig
│           └── exception
│               ├── EmailAlreadyExistsException
│               ├── UserNotFoundException
│               └── GlobalExceptionHandler
│           └── util
                └── ApiMessage
                └── JwtUtil
            └── mapper
                └── UserMapper         
└── test
    └── java
        └── com.sawiya.authservice
            └── controller
                └── AuthControllerWebMvcTest
            └── service
                └── AuthServiceTest
```

## Requirements

Before running the application, make sure you have:

- Java 17 or later
- Maven 3.9+
- PostgreSQL
- A PostgreSQL database

## Database Configuration

Configure the PostgreSQL connection in:

```text
src/main/resources/application.yml
```

Example:

```properties
spring:

    datasource:
        url: ${SPRING_DATASOURCE_URL}
        username: ${DB_USERNAME}
        password: ${DB_PASSWORD}
    jpa:
        hibernate:
            ddl-auto: none
```

Do not commit real database passwords or JWT secrets to source control.

## Running the Application

Clone the repository and navigate to the project directory:

```bash
git clone <repository-url>
cd authservice
```

Run the application with Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Alternatively:

```bash
mvn spring-boot:run
```

The application will start on the configured Spring Boot port, which is normally:

```text
http://localhost:8080
```

# API

### Authentication Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/signin` | Authenticate an existing user and receive a JWT |

## Register

Creates a new user account.

### Request

```http
POST /api/auth/register
Content-Type: application/json
```

Example:

```json
{
  "firstName": "Bruce",
  "lastName": "Wayne",
  "email": "bruce@example.com",
  "password": "Password123!"
}
```

### Successful Response

```http
200 OK
```

```json
{
  "success": true,
  "message": "Registration successful"
}
```

### Validation Error

If the request contains invalid data:

```http
400 Bad Request
```

Example:

```json
{
  "message": "Password must contain at least one special character"
}
```

### Duplicate Email

If the email is already registered:

```http
409 Conflict
```

Example:

```json
{
  "message": "Email already exists"
}
```

## Sign In

Authenticates an existing user and returns a JWT.

### Request

```http
POST /api/auth/signin
Content-Type: application/json
```

Example:

```json
{
  "email": "bruce@example.com",
  "password": "Password123!"
}
```

### Successful Response

```http
200 OK
```

```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1Ni..."
}
```

### Authentication Failure

If the email or password is incorrect:

```http
401 Unauthorized
```

```json
{
  "success": false,
  "message": "Invalid email or password"
}
```

The API does not expose whether the email exists, which avoids unnecessarily revealing account information.

## Validation Rules

### Email

The email must:

- Be provided
- Be a valid email address
- Not already exist during registration

### Password

The password must:

- Be provided
- Contain at least 8 characters
- Contain at least one special character

Example of a valid password:

```text
Password123!
```

Example of an invalid password:

```text
Password123
```

The second password does not contain a special character.

## Authentication

The application uses Spring Security with JWT-based authentication.

The authentication flow is:

```text
Client
   │
   │ POST /api/auth/signin
   ▼
AuthController
   │
   ▼
AuthService
   │
   ▼
AuthenticationManager
   │
   ▼
UserDetailsService
   │
   ▼
UserService
   │
   ▼
UserRepository
   │
   ▼
Database
```

When authentication succeeds, the service generates a JWT and returns it to the client.

The client can then use the token when accessing protected endpoints:

```http
Authorization: Bearer <JWT>
```

The application uses stateless sessions:

```java
SessionCreationPolicy.STATELESS
```

Therefore, the server does not maintain an HTTP session for authenticated users.

## Security

The following endpoints are publicly accessible:

```text
POST /api/auth/signin
POST /api/auth/register
```

Other endpoints require authentication:

```java
.anyRequest().authenticated()
```

Passwords are never stored as plain text. BCrypt is used for password hashing:

```java
new BCryptPasswordEncoder(12)
```

CSRF protection is disabled because the API uses stateless authentication rather than browser-based sessions.

## Error Handling

The application uses a global exception handler to provide consistent HTTP responses.

### Bad Request

```http
400 Bad Request
```

Used for invalid request data and validation errors.

### Unauthorized

```http
401 Unauthorized
```

Used when authentication fails.

Example:

```json
{
  "success": false,
  "message": "Invalid email or password"
}
```

### Conflict

```http
409 Conflict
```

Used when a requested operation conflicts with existing data.

Example:

```json
{
  "message": "Email already exists"
}
```

## Testing

The project uses JUnit 5, Mockito, and Spring MockMvc.

Controller tests use:

```java
@WebMvcTest(AuthController.class)
```

This allows the controller and Spring MVC infrastructure to be tested without requiring a real database.

Services such as `AuthService` and `UserService` are mocked:

```java
@MockBean
private AuthService authService;

@MockBean
private UserService userService;
```

### Run Tests

Run all tests:

```bash
mvn test
```

Run a specific test class:

```bash
mvn -Dtest=AuthControllerWebMvcTest test
```

### Example Test Cases

The controller tests cover scenarios such as:

- Successful registration
- Successful login
- Invalid email
- Missing required fields
- Invalid password
- Password without a special character
- Duplicate email
- Invalid login credentials
- Correct HTTP status codes
- Correct response messages

## Example Login Test

```java
@Test
void signin_shouldReturn200_whenCredentialsAreValid() throws Exception {

    LoginResponseDTO response = new LoginResponseDTO(
            true,
            "Login successful",
            "jwt-token"
    );

    when(authService.authenticate(any(LoginRequestDTO.class)))
            .thenReturn(response);

    mockMvc.perform(
            post("/api/auth/signin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "email": "test@example.com",
                        "password": "Password123!"
                    }
                    """)
    )
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.success").value(true))
    .andExpect(jsonPath("$.message").value("Login successful"))
    .andExpect(jsonPath("$.token").value("jwt-token"));
}
```

## Example Duplicate Email Test

```java
@Test
void register_shouldReturn409_whenEmailAlreadyExists() throws Exception {

    when(userService.register(any(RegisterRequestDTO.class)))
            .thenThrow(new EmailAlreadyExistsException(
                    "Email is already registered"
            ));

    mockMvc.perform(
            post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "firstName": "Bruce",
                        "lastName": "Wayne",
                        "email": "existing@example.com",
                        "password": "Password123!"
                    }
                    """)
    )
    .andExpect(status().isConflict())
    .andExpect(jsonPath("$.message").value("Email already exists"));
}
```

## HTTP Status Codes

| Status | Meaning | Example |
|---|---|---|
| `200 OK` | Request successful | Successful login |
| `400 Bad Request` | Invalid request | Validation failure |
| `401 Unauthorized` | Authentication failed | Invalid credentials |
| `403 Forbidden` | Authenticated but not permitted | Protected resource |
| `409 Conflict` | Resource conflict | Duplicate email |

## Design Notes

The application separates responsibilities between the controller, service, repository, and security layers.

```text
Controller
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
Database
```

Authentication is handled by Spring Security's `AuthenticationManager`, allowing authentication failures to propagate to the global exception handler rather than being manually caught and re-thrown inside the service.

Validation is handled using Jakarta Bean Validation annotations such as:

```java
@NotBlank
@Email
@Size
@Pattern
```

This keeps request validation close to the DTO and prevents invalid requests from reaching the service layer.