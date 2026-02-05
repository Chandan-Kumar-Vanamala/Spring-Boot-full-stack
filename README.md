# Sim Card Activator (Telstra Forage Task 1)

Spring Boot microservice that accepts SIM activation requests, calls the external
SimCard Actuator service, and stores each activation result in a database using JPA.

**What it does**
1. Receives a POST request with `iccid` and `customerEmail`.
2. Calls `http://localhost:8444/actuate` to activate the SIM.
3. Persists the activation result (`iccid`, `customerEmail`, `active`) to the database.
4. Exposes a query endpoint to fetch a stored activation by ID.

**Endpoints**
- `POST /activate`
  - Request body:
    ```json
    { "iccid": "1234567890", "customerEmail": "user@example.com" }
    ```
  - Response:
    ```json
    { "success": true }
    ```
- `GET /query?simCardId=1`
  - Response:
    ```json
    { "iccid": "1234567890", "customerEmail": "user@example.com", "active": true }
    ```

**Run locally**
1. Start the SimCard Actuator service (provided JAR):
   ```bash
   java -jar services/SimCardActuator.jar
   ```
2. Start this service:
   ```bash
   ./mvnw spring-boot:run
   ```

**Tech**
- Java 11
- Spring Boot
- Spring Data JPA
- H2 (runtime)
