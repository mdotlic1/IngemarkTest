# Product management application README

## Introduction
This is a Spring Boot application built with Java 17 and Maven. The application features a RESTful CRUD system and requires a Postgres database for its backend operations.

## Setting Up Locally

### Prerequisites:
- **Java 17**
- **Maven**
- **IntelliJ IDEA** (Recommended IDE)
- **PostgreSQL**
- **Docker** (optional)

### Steps:
1. **Clone the Repository**: Use your preferred Git command to clone the project repository to your local machine.
2. **Open in IntelliJ**: Navigate to the project directory and open the project using IntelliJ.
3. **Database Setup**:
    - Ensure you have a running PostgreSQL instance locally.
    - Update the `resources/dev/application.properties` file with your database name, username, and password. The application will create the necessary tables.
4. **Build the Application**:
    ```bash
    mvn clean install
    ```

## Running via Docker

### Option 1: Local Docker Image:
1. Switch to `dev-docker` profile and build the project:
    ```bash
    mvn clean install -P dev-docker
    ```
2. Build the Docker image (run the command in the same directory as the Dockerfile):
    ```bash
    docker build -t rest-crud .
    ```
3. Pull the Postgress image:
   ```bash
    docker pull postgres:latest
    ```
4. Deploy using Docker Compose (run the command in the same directory as the docker-compose.dev.yml):
    ```bash
    docker-compose -f docker-compose.dev.yml up
    ```

### Option 2: Docker Hub Image:
1. Pull the Docker images (run the command in the same directory as the docker-compose.prod.yml):
    ```bash
    docker-compose -f docker-compose.dev.yml pull
    ```
2. Run the application using Docker Compose:
    ```bash
    docker-compose -f docker-compose.prod.yml up
    ```

## Users and Authentication

### Pre-configured Users:
- **Admin**:
    - **Username/Password**: admin/admin
    - **Access**: All CRUD endpoints
- **User**:
    - **Username/Password**: user/user
    - **Access**: Only GET endpoints

### CSRF Token:
- All GET requests can be executed without a CSRF token.
- For other requests, obtain a CSRF token from `/csrf-token` endpoint using either the `user` or `admin` credentials.
- Send the CSRF token in the request header (with the key: X-CSRF-TOKEN) along with Basic Authentication.

### SWAGGER:
- Swagger is available on `/swagger-ui/index.html`

### Changing Passwords:
Endpoint: `/users/{username}/change-password`
- **Admin**: Can change passwords for other users as well as their own.
- **User**: Can only change their own password.

---

## Conclusion

Ensure all dependencies and prerequisites are properly set up before attempting to run the application. Please report any issues you encounter.

