# Payment System

This project has two main sections:
- **Backend**: Java, Spring Boot, JPA, Hibernate, Flyway migrations
- **Frontend**: Angular


## Requirements

### Backend Requirements
- Java 17+ (JDK installed and configured)
- Maven 3.8+ (for dependency management and build)
- Spring Boot (included via Maven dependencies)
- Postgres (optional)
  - If Postgres is not installed, the project can run with H2 in-memory database
- Flyway (for database migrations, included via Maven)
- Swagger UI (auto-configured for API documentation)

### Frontend Requirements
- Node.js 18+ (runtime environment) — *I used version 20.19.5*
- npm 9+ (package manager) — *I used version 11.2.0*
- Angular CLI 18+ — *I used Angular CLI 21*

### Testing Requirements
- JUnit 5 (backend unit and integration tests)
- Mockito (backend mocking framework)
- Spring Boot Test (integration testing support)
- Karma + Jasmine (Angular unit testing framework)

### Additional Tools
- Git (for cloning the repository)
- Browser (Chrome/Firefox/Edge for accessing Swagger UI and Angular frontend)
- pgAdmin or psql (optional, for inspecting Postgres database directly)

---

## Backend (Spring Boot)

### Setup
After cloning the project, navigate to the root directory of the backend project (`paymentEndpoint`), open command line and run:

```bash
mvn clean install
```

- The project has two profiles:

  - If Postgres is installed:
    ```bash
    mvn spring-boot:run -Dspring-boot.run.profiles=postgres
    ```
  - If Postgres is not installed (use H2 in-memory):
    ```bash
      mvn spring-boot:run -Dspring-boot.run.profiles=h2
      ```
Flyway executes the SQL queries in: 
```code
paymentEndpoint/src/main/resources/db/migration/V1__create_student_account_and_fee_payment.sql
```
This creates the tables `student_account, fee_payments, and flyway_schema_history`.
Data from `data.sql` is seeded into the tables whether you're  using Postgres or H2. To re-run the server, make sure you clear the data in the tables or drop tables if possible, that's if you're using postgres. This is to prevent seeding of data that already exists in the database as it may violate some constraints and cause errors. But with h2, data is lost once servers stops running.

### Using the Application

When the server starts, it runs on port 8080 by default. Visit Swagger UI at: 
```code
http://localhost:8080/swagger-ui/index.html
```
Here you can test the endpoint using seeded data.
Example:

Student number: STUD000001

Payment amount: 100000 (or any value less than 800,000)

Swagger UI will display a response body with payment details (incentive rate, incentive amount, new balance, etc.).

## Frontend (Angular)

### Setup
After cloning the project, navigate to the root directory of the frontend project (payment-frontend), open command line and run:
```bash
npm install
```
to install dependencies, then to start the project:
```bash
npm start
```
The app runs at: 
```code 
http://localhost:4200/
```
### Using the Application

- A form will appear.

- Enter a valid student number (STUD000001, STUD000002, or STUD000003) and a payment amount.

- Click Pay.

- To inspect the response:

  - Right-click the page → Inspect

  - Open the Console tab to see the response body.

- Alert messages will show depending on the situation.

## Testing

### Backend Tests
Run tests with H2 (no Postgres required):

```bash
mvn test -Dspring-boot.run.profiles=h2
```
Run tests with Postgres:

```bash
mvn test -Dspring-boot.run.profiles=postgres
```

### Frontend Tests
From the payment-frontend directory:

```bash
ng test
```