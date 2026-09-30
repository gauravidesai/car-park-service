# Car Parking Service

A simple RESTful API built with Spring Boot for managing a car park. The application allocates vehicles to the first available parking space, calculates parking charges, and manages vehicle entry and exit without using a database.
This project was developed as a take-home coding exercise to demonstrate clean architecture, REST API design, in-memory data management, validation, exception handling, and unit testing.

---

## Features

- Park a vehicle in the first available space
- Retrieve current parking occupancy status
- Calculate parking charges on vehicle exit
- Automatically free parking spaces when a vehicle leaves
- In-memory storage (no database)
- Request validation
- Global exception handling
- Unit tests
- Swagger/OpenAPI documentation

---

## Tech Stack

- **Java 17**
- **Spring Boot 4.0.8**
- **Maven**
- **JUnit 5**
- **Lombok**
- **SLF4J Logging**
- **SpringDoc OpenAPI (Swagger)**

---

## Architecture
The application follows a layered architecture:
```text
Client
   │
   ▼
Controller Layer
   │
   ▼
Service Layer
   │
   ▼
In-Memory Storage
(Map + PriorityQueue)
```
### Components
| Layer | Responsibility |
|---------|---------------|
| Controller | Handles HTTP requests and responses |
| Service | Contains all business logic |
| DTOs | Request and response objects |
| Models | Core domain objects |
| Exceptions | Error handling and API responses |
---

## Project Structure
```text
src/main/java/com/gauravidesai/car_park_service
├── controller
│   └── ParkingController.java
│
├── service
│   └── CarParkingService.java
│
├── dto
│   ├── ParkingRequest.java
│   ├── ParkingResponse.java
│   ├── BillRequest.java
│   ├── BillResponse.java
│   ├── ErrorResponse.java
│   └── OccupiedParkingResponse.java
│
├── model
│   ├── ParkingRecord.java
│   └── VehicleType.java
│
├── exception
│   └── GlobalExceptionMapper.java
│
└── CarParkServiceApplication.java
```
---
## Vehicle Types
| Vehicle Type | Value | Charge Per Minute |
|-------------|--------|------------------|
| Small Car | 1 | £0.10 |
| Medium Car | 2 | £0.20 |
| Large Car | 3 | £0.40 |
### Additional Charges
An additional charge of **£1.00 is applied for every completed 5-minute period parked.**
Example:
```text
Vehicle Type: Medium Car
Duration: 12 minutes

Base Charge:
12 × £0.20 = £2.40

Additional Charge:
floor(12/5) × £1 = £2.00

Total:
£4.40
```
---
## API Endpoints
### 1. Get Parking Status
#### Request
```http
GET /parking
```
#### Response
```json
{
  "availableSpaces": 99,
  "occupiedSpaces": 1
}
```
---
### 2. Park Vehicle
#### Request
```http
POST /parking
```
```json
{
  "vehicleReg": "XX1ZZ5",
  "vehicleType": 2
}
```
#### Response
```json
{
  "vehicleReg": "XX1ZZ5",
  "spaceNumber": 1,
  "timeIn": "2026-09-30T14:19:33.4912803"
}
```
---
### 3. Generate Bill & Exit Vehicle
#### Request
```http
POST /parking/bill
```
```json
{
  "vehicleReg": "XX1ZZ5"
}
```
#### Response
```json
{
  "billId": "cadd2f4f-d41c-4da1-a81a-8c965df2446d",
  "vehicleReg": "XX1ZZ5",
  "vehicleCharge": 0.0,
  "timeIn": "2026-09-30T14:17:59.8394029",
  "timeOut": "2026-09-30T14:18:25.6817935"
}
```
---
## Key Design Decisions
### In-Memory Storage
The requirements explicitly requested no database usage.
The solution uses:
```java
ConcurrentHashMap<String, ParkingRecord>
```
to store parked vehicles.
### Space Allocation
The first available parking space is allocated using:
```java
PriorityQueue<Integer>
```
which always returns the lowest available space number.

### Thread Safety
Parking and exit operations are synchronized to prevent race conditions during concurrent requests.

---

## Error Handling
The API handles common scenarios such as:
- Invalid vehicle type
- Duplicate vehicle registration
- Full car park
- Vehicle not found
- Validation errors
Example:
```json
{
  "message": "Vehicle already parked"
}
```
---
## Unit Testing
Example test scenarios:
- Allocate first parking space
- Car park full
- Duplicate registration
- Vehicle exit
- Bill calculation
- Parking status retrieval
Run tests:
```bash
 ./mvnw test
```
---
## Running the Application
### Clone Repository
```bash
git clone https://github.com/gauravidesai/car-park-service.git
```
### Navigate to Project
```bash
cd car-park-service
```
### Start Application
```bash
./mvnw spring-boot:run
```
Application will start on:
```text
http://localhost:8080
```

---
## Swagger Documentation
After starting the application:
```text
http://localhost:8080/swagger-ui/index.html
```

---
## Future Improvements
If this were a production application, the following improvements could be added:
- PostgreSQL persistence
- Flyway database migrations
- Docker support
- Authentication & Authorization
- Structured logging
- Metrics & monitoring
- Integration tests
- CI/CD pipeline
---

## Author
**Gauravi Desai**
Java Backend Developer
- Java
- Spring Boot
- Quarkus
- REST APIs
- OpenAPI
- Microservices 
- GitHub: https://github.com/gauravidesai
---
## License
This project is provided for demonstration and assessment purposes.