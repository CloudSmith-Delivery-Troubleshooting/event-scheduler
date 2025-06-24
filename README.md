# Event Scheduler Application

A medium-sized Spring Boot application demonstrating an event scheduling system with clean architecture, time abstraction, shared state, and external dependency simulation. The application uses a flexible clock abstraction to enable frozen or manipulated clock scenarios, supports RESTful APIs, and includes comprehensive unit tests.

---

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Continuous Integration](#continuous-integration)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- Create, query, and complete scheduled events.
- Time-dependent logic via a centralized `SystemClock` abstraction.
- Shared mutable state simulation with a thread-safe counter.
- External dependency simulation with a notification service.
- REST API built with Spring Boot.
- Persistence with Spring Data JPA and in-memory H2 database.
- Comprehensive unit tests using JUnit 5 and Mockito.
- CI workflow with GitHub Actions for automated build and testing.

---

## Technologies Used

- Java 17
- Spring Boot 3.0.6
- Spring Data JPA
- H2 Database (in-memory)
- Lombok
- JUnit 5
- Mockito
- Maven
- GitHub Actions (CI)

---

## Getting Started

### Prerequisites

- Java 17 JDK installed
- Maven 3.6+
- Git (optional, for cloning repository)

### Clone the repository

```angular2html
git clone https://github.com/saurabhbothra22/event-scheduler.git
cd event-scheduler
```

### Build the project

```angular2html
mvn clean package
```


---

## Running the Application

Run the Spring Boot application using Maven:

```angular2html
mvn spring-boot:run
```


The application will start on `http://localhost:8080`.

---

## API Endpoints

| Method | Endpoint                | Description                      | Parameters                      |
|--------|-------------------------|--------------------------------|--------------------------------|
| POST   | `/api/events`           | Create a new event              | `name` (String), `scheduledTime` (ISO-8601 String) |
| GET    | `/api/events/due`       | Get events scheduled before now | None                           |
| POST   | `/api/events/{id}/complete` | Mark event as completed          | `id` (Long)                    |

### Example: Create Event

```angular2html
curl -X POST "http://localhost:8080/api/events?name=MyEvent&scheduledTime=2025-06-23T18:00:00Z"
```


### Example: Get Due Events

```angular2html
curl "http://localhost:8080/api/events/due"
```


### Example: Complete Event

```angular2html
curl -X POST "http://localhost:8080/api/events/1/complete"
```


---

## Testing

Run unit tests with Maven:

```angular2html
mvn test
```


The project includes tests for:

- Event service logic
- Shared counter behavior
- Clock abstraction correctness
- Notification service mocking

---

## Continuous Integration

This project uses GitHub Actions for CI with the following workflow:

- Checks out code
- Sets up JDK 17
- Caches Maven dependencies
- Builds the project
- Runs unit tests

Workflow file: `.github/workflows/ci.yml`


---

## Contributing

Contributions are welcome! Please:

- Fork the repository
- Create a feature branch
- Commit your changes with clear messages
- Submit a pull request

Make sure all tests pass before submitting.

---

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.

---

## Acknowledgments

- Inspired by best practices in Spring Boot application design.
- Uses ideas from enterprise architecture patterns and time-dependent testing strategies.

---

*Happy coding! 🚀*


