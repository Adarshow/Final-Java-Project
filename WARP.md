# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

Project stack
- Java 17, Spring Boot 3.2 (spring-boot-starter-parent)
- Maven build (pom.xml)
- Spring Data JPA + MySQL
- Thymeleaf templates, Bootstrap via WebJars
- Spring Security with role-based access control

Common commands (PowerShell)
- Build (skip tests):
```powershell path=null start=null
mvn -q -DskipTests package
```
- Clean build:
```powershell path=null start=null
mvn -q clean package
```
- Run the app (development):
```powershell path=null start=null
mvn spring-boot:run
```
- Run the packaged JAR (after build):
```powershell path=null start=null
java -jar target/healthcare-system-0.0.1-SNAPSHOT.jar
```
- Run tests:
```powershell path=null start=null
mvn -q test
```
- Run a single test (Maven Surefire):
```powershell path=null start=null
# Class
mvn -q -Dtest=SomeTest test

# Single method
mvn -q -Dtest=SomeTest#someMethod test
```
- Lint/format: Not configured in pom.xml. If needed, add Checkstyle/Spotless plugins.

Local database configuration
The application expects a local MySQL database. You can override Spring datasource settings with environment variables (recommended). Replace placeholders before running.
```powershell path=null start=null
$env:SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/healthcare_db?useSSL=false&serverTimezone=UTC"
$env:SPRING_DATASOURCE_USERNAME="{{DB_USERNAME}}"
$env:SPRING_DATASOURCE_PASSWORD="{{DB_PASSWORD}}"

mvn spring-boot:run
```
Ensure the database exists before running. Example (MySQL client):
```sql path=null start=null
CREATE DATABASE IF NOT EXISTS healthcare_db;
```
Optional: run MySQL locally via Docker
```powershell path=null start=null
# Exposes MySQL on localhost:3306 and creates healthcare_db
# Set a secure root password via {{MYSQL_ROOT_PASSWORD}}
docker run -d --name healthcare-mysql -p 3306:3306 `
  -e MYSQL_ROOT_PASSWORD={{MYSQL_ROOT_PASSWORD}} `
  -e MYSQL_DATABASE=healthcare_db `
  mysql:8
```

High-level architecture
- Entry point
  - com.healthcare.HealthcareSystemApplication is the Spring Boot main class.
- Web layer (Controllers)
  - com.healthcare.controller.* defines MVC endpoints for core domains (e.g., Home, Patient, Doctor, Appointment, Prescription, Billing, Signup). Controllers render Thymeleaf views in src/main/resources/templates.
- Service layer
  - com.healthcare.service.* encapsulates business logic and orchestrates repositories/entities. Examples include AppointmentService, PatientService, DoctorService, PharmacyService, BillingService, PrescriptionService, UserService.
- Data access layer
  - com.healthcare.repository.* uses Spring Data JPA repositories (e.g., UserRepository) to persist JPA entities.
- Domain model
  - com.healthcare.entity.* contains JPA entities (Patient, Doctor, Appointment, Prescription, Billing, Staff, User, Pharmacy, etc.) with relationships (e.g., @ManyToOne, @OneToMany) and enums for statuses/roles.
- Security
  - com.healthcare.config.SecurityConfig defines the SecurityFilterChain, role-based authorization per route, form login at /login with default success redirect to /dashboard, and PasswordEncoder bean (BCrypt).
  - User implements UserDetails and maps roles: ADMIN, DOCTOR, STAFF, PATIENT.
- Views and static assets
  - Templates in src/main/resources/templates (e.g., layout, index, login, signup, per-feature subfolders).
  - Static assets under src/main/resources/static (e.g., css/styles.css). Bootstrap and icons are included via WebJars/CDN.
- Configuration
  - src/main/resources/application.yml sets datasource, JPA, Thymeleaf, logging. Prefer overriding secrets and environment-specific values via environment variables at runtime.

Notes for Warp
- Prefer mvn commands (no Maven Wrapper present).
- On Windows/pwsh, use the PowerShell blocks above. For other shells, adapt environment variable syntax accordingly.
- There are no tests indexed under src/test at the moment; Maven test goals will no-op until tests are added.
