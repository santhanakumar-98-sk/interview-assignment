# Meeting Room Booking Service

## Overview

This project is a **Spring Boot application** for managing meeting rooms and bookings.
It allows users to:

* Create and manage meeting rooms
* Book rooms for specific time slots
* Cancel bookings
* View room utilization reports

---

## Tech Stack

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 (for testing)
* MySQL (for production)
* JUnit 5 & MockMvc (for testing)

---

## Project Structure

```
src
 ├── main
 │    └── java/... (controllers, services, repositories, entity, dto, util, exception)
 └── test
      ├── java/... (test cases)
      └── resources
           └── application-test.properties
```

---

##  Test Suite

Controller-level tests are implemented using **MockMvc**.

### Covered Modules

#### RoomControllerTest

* Create Room
* List Rooms
* Validation (invalid input)

#### BookingControllerTest

* Create Booking
* Idempotent Booking
* List Bookings (pagination response)
* Cancel Booking

#### ReportControllerTest

* Room Utilization Report

---

## Test Approach

* Uses `@SpringBootTest` and `MockMvc`
* Uses **H2 in-memory database** for testing
* Each test is **independent**
* Required data is created using `@BeforeEach`
* Covers:

  * Success scenarios
  * Validation scenarios
  * API response structure

---

## How to Run the Application

### Using Maven

```bash
mvn spring-boot:run
```

Application will start at:

```
http://localhost:8080
```

---

## How to Run Tests

### Using IDE

1. Open project in IntelliJ / Eclipse
2. Navigate to:

   ```
   src/test/java
   ```
3. Right-click on test class or folder
4. Click **Run Tests**

---

### Using Maven

```bash
mvn test
```

---

## API Endpoints

### Room APIs

* `POST /api/v1/rooms` → Create Room
* `GET /api/v1/rooms` → List Rooms

### Booking APIs

* `POST /api/v1/bookings` → Create Booking
* `GET /api/v1/bookings` → List Bookings
* `POST /api/v1/bookings/{id}/cancel` → Cancel Booking

### Report APIs

* `GET /api/v1/reports/room-utilization` → Room Utilization Report

---

## Notes

* Tests do not depend on external database
* Each test runs independently
* API behavior is verified using status codes and response structure

---

## Conclusion

This project demonstrates:

* REST API design using Spring Boot
* Proper layering (Controller → Service → Repository)
* Basic test coverage using MockMvc
* Handling real-world scenarios like idempotency and validation

---
