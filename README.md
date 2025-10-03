# Healthcare Administration System

## Project Description
The Healthcare Administration System is a web-based application designed to manage various aspects of a healthcare facility. It provides functionalities for managing patients, doctors, appointments, prescriptions, and billing. The system aims to streamline administrative tasks, improve data organization, and enhance the overall efficiency of healthcare operations.

## Features
*   **User Authentication and Authorization**: Secure login and role-based access control for different types of users (e.g., administrators, doctors, patients).
*   **Patient Management**: Register new patients, view patient details, and update patient information.
*   **Doctor Management**: Manage doctor profiles, including specialization and contact information.
*   **Appointment Scheduling**: Schedule, view, and manage appointments for patients with doctors.
*   **Prescription Management**: Create, view, and manage prescriptions for patients.
*   **Billing and Invoicing**: Generate and manage invoices for healthcare services.
*   **Responsive User Interface**: A user-friendly interface built with Thymeleaf and Bootstrap.

## Technologies Used
*   **Spring Boot**: Framework for building robust, production-ready Spring applications.
*   **Spring Security**: Provides authentication and authorization features.
*   **Spring Data JPA**: Simplifies database access and persistence with Hibernate.
*   **Thymeleaf**: Server-side Java template engine for web and standalone environments.
*   **MySQL**: Relational database for storing application data.
*   **Maven**: Build automation tool for Java projects.
*   **Bootstrap**: Front-end framework for responsive and mobile-first web development.

## Setup and Installation

### Prerequisites
*   Java Development Kit (JDK) 17 or higher
*   Maven 3.6.0 or higher
*   MySQL Server
*   Git

### Steps
1.  **Clone the repository**:
    ```bash
    git clone <YOUR_GITHUB_REPOSITORY_URL>
    cd healthcare-system
    ```
    Replace `<YOUR_GITHUB_REPOSITORY_URL>` with the actual URL of this GitHub repository.

2.  **Database Configuration**:
    *   Create a MySQL database named `healthcare_db`.
    *   The application will automatically create tables if `spring.jpa.hibernate.ddl-auto` is set to `update` in `src/main/resources/application.yml`.
    *   Update the database credentials in `src/main/resources/application.yml`:
        ```yaml
        spring:
          datasource:
            url: jdbc:mysql://localhost:3306/healthcare_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
            username: root
            password: your_mysql_password
            driver-class-name: com.mysql.cj.jdbc.Driver
        ```
        Replace `your_mysql_password` with your MySQL root password.

3.  **Build the project**:
    ```bash
    mvn clean install
    ```

4.  **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

## Usage
Once the application is running, open your web browser and navigate to `http://localhost:8080`.

*   **Signup**: New users can register for an account.
*   **Login**: Existing users can log in with their credentials.
*   **Default Credentials**:
    ADMIN:
    (Username = admin,
    Password = admin)
    DOCTOR:
    (Username = doctor,
    Password = doctor)
    STAFF:
    (Username = staff,
    Password = staff)
*   **Dashboard**: After logging in, users will be redirected to a dashboard where they can access different modules based on their roles.
