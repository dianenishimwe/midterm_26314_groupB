<<<<<<< HEAD
=======
# midterm_26314_groupB
>>>>>>> 841e1576652314a3180f23428bfa61400ff38fe0
# Bus Management System

A comprehensive Spring Boot application for managing bus operations, routes, users, and Rwanda's administrative locations.

## 📋 Table of Contents
- [Features](#features)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Testing with Postman](#testing-with-postman)

## ✨ Features

- **Location Management**: Hierarchical location system (Province → District → Sector → Cell → Village)
- **Bus Management**: Track buses, their status, capacity, and assigned routes
- **Route Management**: Manage bus routes with start/end locations and distances
- **User Management**: Handle users with different roles (Admin, Driver, Passenger)
- **Booking System**: User-route booking with seat assignments
- **Driver License Management**: Track driver licenses and expiry dates
- **RESTful APIs**: Complete CRUD operations for all entities
- **Security**: BCrypt password encryption
- **Data Persistence**: PostgreSQL database with JPA/Hibernate

## 🛠 Technologies

- **Java 17**
- **Spring Boot 4.0.3**
- **Spring Data JPA**
- **Spring Security**
- **PostgreSQL 16**
- **Hibernate 7.2.4**
- **Lombok**
- **Maven**

## 📦 Prerequisites

Before running this application, ensure you have:

- Java 17 or higher installed
- PostgreSQL 16 installed and running
- Maven 3.6+ installed
- Postman (for API testing)

## 🚀 Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd busmanagementmidterm/busmanagement/busmanagement
```

### 2. Create PostgreSQL Database
```sql
CREATE DATABASE busmanagement;
```

### 3. Configure Database Connection
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/busmanagement
spring.datasource.username=postgres
spring.datasource.password=admin
```

### 4. Build the Project
```bash
mvn clean install
```

## ⚙️ Configuration

### Application Properties
Located at: `src/main/resources/application.properties`

```properties
# Application Name
spring.application.name=busmanagement

# Server Port
server.port=8081

# PostgreSQL Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/busmanagement
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=admin

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# SQL Logging
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
logging.level.org.hibernate.SQL=DEBUG
```

## 🏃 Running the Application

### Using Maven
```bash
mvn spring-boot:run
```

### Using Java
```bash
mvn clean package
java -jar target/busmanagement-0.0.1-SNAPSHOT.jar
```

The application will start on: **http://localhost:8081**

## 📡 API Endpoints

### Location APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/locations/all` | Get all locations |
| GET | `/api/locations` | Get locations (paginated) |
| GET | `/api/locations/{id}` | Get location by ID |
| GET | `/api/locations/code/{code}` | Get location by code |
| GET | `/api/locations/name/{name}` | Get location by name |
| GET | `/api/locations/provinces` | Get all provinces |
| GET | `/api/locations/type/{type}` | Get locations by type (PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE) |
| GET | `/api/locations/{parentId}/children` | Get child locations |
| GET | `/api/locations/{id}/hierarchy-path` | Get location hierarchy path |
| GET | `/api/locations/{id}/villages` | Get all villages under location |
| GET | `/api/locations/search?keyword={keyword}` | Search locations |
| GET | `/api/locations/statistics` | Get location statistics |
| POST | `/api/locations/parent` | Create parent location (Province) |
| POST | `/api/locations/child?parentId={id}` | Create child location |
| PUT | `/api/locations/{id}` | Update location |
| DELETE | `/api/locations/{id}` | Delete location |

### User APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register new user |
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/email/{email}` | Get user by email |
| GET | `/api/users/username/{username}` | Get user by username |
| GET | `/api/users/exists/email/{email}` | Check if email exists |
| GET | `/api/users/exists/username/{username}` | Check if username exists |
| GET | `/api/users/paginated` | Get users (paginated) |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

### Bus APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/buses` | Create new bus |
| GET | `/api/buses` | Get all buses |
| GET | `/api/buses/{id}` | Get bus by ID |
| GET | `/api/buses/exists/plate/{plateNumber}` | Check if plate exists |
| GET | `/api/buses/status/{status}` | Get buses by status (ACTIVE, MAINTENANCE, INACTIVE) |
| GET | `/api/buses/route/{routeId}` | Get buses by route |
| GET | `/api/buses/paginated` | Get buses (paginated) |
| PUT | `/api/buses/{id}` | Update bus |
| DELETE | `/api/buses/{id}` | Delete bus |

### Route APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/routes` | Create new route |
| GET | `/api/routes` | Get all routes |
| GET | `/api/routes/{id}` | Get route by ID |
| PUT | `/api/routes/{id}` | Update route |
| DELETE | `/api/routes/{id}` | Delete route |

## 🗄️ Database Schema

### Tables

1. **locations** - Hierarchical location data
   - id (UUID, PK)
   - code (VARCHAR, UNIQUE)
   - name (VARCHAR)
   - location_type (ENUM: PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE)
   - description (VARCHAR)
   - parent_id (UUID, FK → locations)

2. **users** - User accounts
   - id (BIGINT, PK)
   - username (VARCHAR, UNIQUE)
   - password (VARCHAR)
   - email (VARCHAR, UNIQUE)
   - full_name (VARCHAR)
   - role (ENUM: ADMIN, DRIVER, PASSENGER)

3. **buses** - Bus fleet
   - id (BIGINT, PK)
   - plate_number (VARCHAR, UNIQUE)
   - model (VARCHAR)
   - capacity (INTEGER)
   - status (ENUM: ACTIVE, MAINTENANCE, INACTIVE)
   - route_id (BIGINT, FK → routes)

4. **routes** - Bus routes
   - id (BIGINT, PK)
   - route_number (VARCHAR, UNIQUE)
   - start_location (VARCHAR)
   - end_location (VARCHAR)
   - distance (DOUBLE)
   - estimated_duration (DOUBLE)

5. **user_routes** - Booking records
   - id (BIGINT, PK)
   - user_id (BIGINT, FK → users)
   - route_id (BIGINT, FK → routes)
   - seat_number (VARCHAR)
   - booking_date (TIMESTAMP)
   - status (ENUM: CONFIRMED, CANCELLED, COMPLETED)

6. **driver_licenses** - Driver license information
   - id (BIGINT, PK)
   - user_id (BIGINT, FK → users)
   - license_number (VARCHAR, UNIQUE)
   - license_category (VARCHAR)
   - issue_date (DATE)
   - expiry_date (DATE)

## 🧪 Testing with Postman

### 1. Get All Locations
```
GET http://localhost:8081/api/locations/all
```

### 2. Get Location Statistics
```
GET http://localhost:8081/api/locations/statistics
```
**Response:**
```json
{
    "provinceCount": 4,
    "districtCount": 6,
    "sectorCount": 5,
    "cellCount": 4,
    "villageCount": 4
}
```

### 3. Register a User
```
POST http://localhost:8081/api/users/register
Content-Type: application/json

{
    "name": "John Doe",
    "email": "john@example.com",
    "username": "johndoe",
    "password": "password123",
    "role": "PASSENGER"
}
```

### 4. Create a Bus
```
POST http://localhost:8081/api/buses
Content-Type: application/json

{
    "plateNumber": "RAD123A",
    "model": "Mercedes Benz",
    "capacity": 50,
    "status": "ACTIVE",
    "routeId": 1
}
```

### 5. Get All Routes
```
GET http://localhost:8081/api/routes
```

## 📊 Pre-loaded Data

The application automatically loads sample data on startup:

### Locations (23 total)
- **4 Provinces**: Kigali City, Northern Province, Eastern Province, Southern Province
- **6 Districts**: Gasabo, Nyarugenge, Kicukiro, Musanze, Kayonza, Huye
- **5 Sectors**: Remera, Kacyiru, Kinigi, Mukarange, Tumba
- **4 Cells**: Gisimenti, Nyange, Nyagatovu, Cyarwa
- **4 Villages**: Kigali Village 1, Nyange, Kazirabwayi, Agahora

### Routes (2 total)
- RT001: Kigali → Huye (125.5 km, 2.5 hours)
- RT002: Kigali → Musanze (78.3 km, 1.5 hours)

## 🔒 Security

- Password encryption using BCrypt
- All endpoints are publicly accessible (for development)
- Production deployment should implement proper authentication/authorization

## 🐛 Troubleshooting

### Port Already in Use
If port 8081 is occupied, change it in `application.properties`:
```properties
server.port=8082
```

### Database Connection Error
Ensure PostgreSQL is running and credentials are correct:
```bash
# Check PostgreSQL status
pg_ctl status

# Start PostgreSQL
pg_ctl start
```

### Build Errors
Clean and rebuild:
```bash
mvn clean install -U
<<<<<<< HEAD
```

## 📝 License

This project is for educational purposes.

## 👥 Contributors

- Your Name

## 📧 Contact

For questions or support, contact: your.email@example.com

---

**Built with ❤️ using Spring Boot**
=======

>>>>>>> 841e1576652314a3180f23428bfa61400ff38fe0
