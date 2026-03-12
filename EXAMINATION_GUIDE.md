# Bus Management System - Examination Implementation Guide

## Project Overview
This Spring Boot application demonstrates Rwanda's administrative structure with proper entity relationships and all examination requirements.

---

## Entity Relationship Diagram (ERD) - 3 Marks

### Tables Created (6 Tables):

1. **provinces** - Top administrative level (5 provinces)
2. **districts** - Second level (foreign key: province_id)
3. **sectors** - Third level (foreign key: district_id)
4. **cells** - Fourth level (foreign key: sector_id)
5. **villages** - Fifth/lowest level (foreign key: cell_id)
6. **users** - Linked to villages (foreign key: village_id)
7. **driver_licenses** - One-to-One with users (foreign key: user_id)
8. **routes** - Bus routes
9. **user_routes** - Many-to-Many join table between users and routes

### Relationship Hierarchy:
```
Province (1) → (Many) District
District (1) → (Many) Sector
Sector (1) → (Many) Cell
Cell (1) → (Many) Village
Village (1) → (Many) User
User (1) → (1) DriverLicense [One-to-One]
User (M) → (M) Route [Many-to-Many through UserRoute]
```

**Key Point:** Users are linked ONLY to Village, not directly to Province. This allows automatic traversal through the hierarchy to retrieve users by any administrative level.

---

## 1. Implementation of Saving Location - 2 Marks

### How Data is Stored:
**Location:** `VillageController.saveVillage()` → `VillageService.saveVillage()`

**Process:**
1. Controller receives `VillageDTO` from HTTP POST request
2. Service fetches the parent `Cell` entity using `cellId`
3. Creates `Village` entity with `Cell` reference
4. `Repository.save()` persists the entity:
   - Opens database transaction (`@Transactional`)
   - JPA's `EntityManager` generates INSERT SQL
   - Foreign key (`cell_id`) is automatically set
   - Transaction commits
5. Returns saved entity with generated ID

### How Relationships are Handled:
- **Many-to-One:** Village has `@ManyToOne(fetch = FetchType.LAZY)` relationship with Cell
- **Foreign Key:** Stored in `villages` table as `cell_id`
- **Cascade:** Operations don't cascade from Village to Cell (Cell must exist first)
- **Traversal:** Can access full hierarchy: Village → Cell → Sector → District → Province

**Example Request:**
```json
POST /api/villages
{
  "name": "Rusororo",
  "code": "01010201",
  "cellId": 1
}
```

---

## 2. Sorting and Pagination - 5 Marks

### How Sorting is Implemented:

**Using Spring Data JPA's Sort class:**
```java
Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
Pageable pageable = PageRequest.of(page, size, sort);
return repository.findAll(pageable);
```

**Endpoints:**
- `GET /api/users/sorted?sortBy=name&ascending=true`
- `GET /api/routes/sorted?sortBy=routeNumber&ascending=false`

### How Pagination Works:

**Implementation:**
1. `PageRequest.of(page, size, sort)` creates pagination configuration
2. Repository method returns `Page<T>` instead of `List<T>`
3. Spring Data JPA executes two queries:
   - **COUNT query:** Gets total number of records
   - **SELECT query:** Applies LIMIT and OFFSET for current page
4. Page object contains:
   - `content`: Actual data for current page
   - `totalElements`: Total count
   - `totalPages`: Calculated based on page size
   - `number`: Current page number
   - `first`, `last`: Boolean flags

**Endpoints:**
- `GET /api/users/paginated?page=0&size=10&sortBy=email&ascending=true`
- `GET /api/routes/paginated?page=0&size=5&sortBy=distance&ascending=false`

### Performance Benefits:
1. **Reduces Memory Usage:** Only loads 'size' records instead of entire table
2. **Improves Response Time:** Smaller result sets are faster to fetch and serialize
3. **Better UI Experience:** Users see first page quickly while more load on demand
4. **Database Optimization:** Can use indexed columns for sorting
5. **Scalability:** Application can handle millions of records without performance degradation

---

## 3. Many-to-Many Relationship - 3 Marks

### Implementation:
**Entities:** User ↔ Route through **UserRoute** join entity

**Why Join Entity Instead of @ManyToMany?**
- Allows storing additional information: bookingDate, seatNumber, status
- More flexible than direct `@ManyToMany` annotation

**Structure:**
```java
// UserRoute.java
@Entity
public class UserRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;
    
    private LocalDateTime bookingDate;
    private String seatNumber;
    private BookingStatus status;
}
```

### Join Table Explanation:
**Table Name:** `user_routes`
**Columns:**
- `id` (Primary Key)
- `user_id` (Foreign Key → users.id)
- `route_id` (Foreign Key → routes.id)
- `booking_date`, `seat_number`, `status` (Additional attributes)

**Bidirectional Navigation:**
- From User: `user.getBookedRoutes()` → List<UserRoute>
- From Route: `route.getUserRoutes()` → List<UserRoute>

---

## 4. One-to-Many Relationship - 2 Marks

### Implementation Example 1: Province → Districts
```java
// Province.java
@OneToMany(mappedBy = "province", cascade = CascadeType.ALL, orphanRemoval = true)
private List<District> districts;

// District.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "province_id", nullable = false)
private Province province;
```

### Implementation Example 2: Village → Users
```java
// Village.java
@OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
private List<User> users;

// User.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "village_id", nullable = false)
private Village village;
```

### Foreign Key Usage:
- **Foreign key stored in:** Child table (the "Many" side)
- **Example:** `districts` table has `province_id` column
- **mappedBy attribute:** Indicates that the other entity owns the relationship
- **CascadeType.ALL:** Operations on Province propagate to Districts
- **FetchType.LAZY:** Children are loaded only when accessed (performance optimization)

---

## 5. One-to-One Relationship - 2 Marks

### Implementation: User ↔ DriverLicense
```java
// User.java
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
private DriverLicense driverLicense;

// DriverLicense.java
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false, unique = true)
private User user;
```

### How Entities are Connected:
- **Foreign Key:** `user_id` in `driver_licenses` table
- **Unique Constraint:** `unique = true` ensures one-to-one (not one-to-many)
- **Ownership:** DriverLicense owns the relationship (has the foreign key)
- **Bidirectional:** Can navigate from User to License and vice versa
- **Cascade:** Saving a User with license automatically saves the license

**Example:**
```java
User user = new User();
DriverLicense license = new DriverLicense();
license.setUser(user);
user.setDriverLicense(license);
userRepository.save(user); // Saves both entities!
```

---

## 6. existsBy() Method - 2 Marks

### Implementation in UserRepository:
```java
boolean existsByEmail(String email);
boolean existsByPhone(String phone);
```

### Implementation in Other Repositories:
```java
// ProvinceRepository
boolean existsByCode(String code);

// DriverLicenseRepository
boolean existsByLicenseNumber(String licenseNumber);

// RouteRepository
boolean existsByRouteNumber(String routeNumber);
```

### How Existence Checking Works:
**Spring Data JPA Query Derivation:**
- Method name follows pattern: `existsBy` + FieldName
- Spring automatically generates query: 
  ```sql
  SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END 
  FROM users u WHERE u.email = ?
  ```

**Efficiency:**
- Does NOT fetch entire entity
- Uses SQL EXISTS or COUNT for optimized checking
- Returns primitive boolean (no null handling needed)

**Usage Example:**
```java
if (userService.existsByEmail(email)) {
    throw new RuntimeException("Email already exists");
}
```

**Endpoint:**
- `GET /api/users/exists/email/{email}` → `{ "exists": true }`

---

## 7. Retrieve Users by Province - 4 Marks

### Requirement:
Retrieve all users from a given province using **province code OR province name**.

### Implementation:

**Method 1: Derived Query Method**
```java
// UserRepository.java
List<User> findByVillageCellSectorDistrictProvinceCodeOrVillageCellSectorDistrictProvinceName(
    String provinceCode, 
    String provinceName
);
```

**Method 2: @Query Annotation**
```java
@Query("SELECT u FROM User u JOIN u.village v JOIN v.cell c JOIN c.sector s JOIN s.district d JOIN d.province p " +
       "WHERE p.code = :code OR p.name = :name")
List<User> findUsersByProvince(@Param("code") String code, @Param("name") String name);
```

### Query Logic Explanation:

**Relationship Traversal Chain:**
```
User → Village → Cell → Sector → District → Province
```

**How it Works:**
1. Start from User entity
2. Navigate through `village` property to reach Village
3. From Village, navigate through `cell` to reach Cell
4. Continue through `sector` → `district` → `province`
5. Apply OR condition on province properties:
   - `province.code = :code` OR
   - `province.name = :name`
6. Return all matching users

**SQL Equivalent:**
```sql
SELECT u.* 
FROM users u
JOIN villages v ON u.village_id = v.id
JOIN cells c ON v.cell_id = c.id
JOIN sectors s ON c.sector_id = s.id
JOIN districts d ON s.district_id = d.id
JOIN provinces p ON d.province_id = p.id
WHERE p.code = '01' OR p.name = 'Kigali City'
```

### Repository Method Used:
- **Primary:** `findByVillageCellSectorDistrictProvinceCodeOrVillageCellSectorDistrictProvinceName()`
- **Alternative:** `findUsersByProvince()` with JPQL query

### Endpoint:
```
GET /api/users/province/search?provinceCode=01&provinceName=Kigali City
```

**Example Usage:**
- `/api/users/province/search?provinceCode=01` → Returns all users from Kigali City
- `/api/users/province/search?provinceName=Eastern Province` → Returns all users from Eastern Province
- Both parameters can be provided; OR logic applies

---

## Viva-Voce Theory Questions - 7 Marks

### 1. How does JPA handle entity relationships?
**Answer:**
JPA uses annotations to map object relationships to database foreign keys:
- **@OneToOne:** Unique foreign key with constraint
- **@OneToMany/@ManyToOne:** Foreign key in child table
- **@ManyToMany:** Join table with two foreign keys
- **FetchType:** LAZY (load on demand) vs EAGER (load immediately)
- **CascadeType:** Propagates operations (PERSIST, MERGE, DELETE, etc.)

### 2. What is the difference between FetchType.LAZY and EAGER?
**Answer:**
- **LAZY:** Loads related entities only when accessed (getter called)
  - Better performance
  - Reduces initial load time
  - Requires open session/transaction
  
- **EAGER:** Loads related entities immediately with parent
  - Simpler code (no lazy initialization exceptions)
  - Can cause performance issues with large datasets
  - Default for OneToOne and ManyToOne

### 3. How does pagination improve performance?
**Answer:**
- **Memory Efficiency:** Loads only subset of data into memory
- **Faster Queries:** LIMIT/OFFSET reduce database response time
- **Network Efficiency:** Smaller payloads over network
- **UI Responsiveness:** Users see first page quickly
- **Scalability:** Handles large datasets without degradation
- **Resource Utilization:** Reduces JVM heap pressure

### 4. Explain Spring Data JPA query derivation
**Answer:**
Spring Data JPA automatically generates queries from method names:
- **Pattern:** `find|read|get` + `By` + `FieldName` + `Conditions`
- **Examples:**
  - `findByEmail(String email)` → `SELECT u FROM User u WHERE u.email = ?`
  - `existsByName(String name)` → `SELECT COUNT(*) > 0 FROM User WHERE name = ?`
  - `findByProvinceCodeOrName(String code, String name)` → `... WHERE code = ? OR name = ?`
- **Benefits:** No manual query writing, type-safe, auto-completion

### 5. What are cascade types and when to use them?
**Answer:**
Cascade types determine how operations propagate to related entities:
- **CascadeType.PERSIST:** Save operation cascades
- **CascadeType.MERGE:** Update operation cascades
- **CascadeType.REMOVE:** Delete operation cascades
- **CascadeType.ALL:** All operations cascade
- **CascadeType.REFRESH:** Refresh operation cascades

**When to Use:**
- **Parent-Child relationships:** Save parent → save children automatically
- **OneToOne/OneToMany:** Often use ALL
- **ManyToOne/ManyToMany:** Usually avoid cascade to prevent unintended side effects

### 6. How do you handle transactions in Spring Boot?
**Answer:**
Using `@Transactional` annotation:
- **Service Layer:** Mark methods with `@Transactional`
- **Read Operations:** `@Transactional(readOnly = true)` for optimization
- **Write Operations:** Default (readOnly = false)
- **Propagation:** Controls transaction boundaries (REQUIRED, REQUIRES_NEW)
- **Rollback:** Automatic on RuntimeException, configurable for checked exceptions

**Example:**
```java
@Service
@Transactional
public class UserService {
    @Transactional(readOnly = true)
    public List<User> getAllUsers() { ... }
    
    @Transactional
    public User saveUser(User user) { ... }
}
```

### 7. Explain the difference between @JoinColumn and mappedBy
**Answer:**

**@JoinColumn:**
- Specifies the foreign key column in database
- Used by the entity that OWNS the relationship
- Physically stores the foreign key value
- Example: `@JoinColumn(name = "province_id")`

**mappedBy:**
- Indicates this entity does NOT own the relationship
- References the owning entity's property name
- No foreign key column created
- Bidirectional navigation only
- Example: `@OneToMany(mappedBy = "province")`

**Rule:** Only ONE side can own the relationship (have @JoinColumn), the other must use mappedBy.

---

## Running the Application

### Prerequisites:
- Java 17+
- Maven

### Start Application:
```bash
cd busmanagement
mvn spring-boot:run
```

### Access Points:
- **Application:** http://localhost:8080
- **H2 Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:busmanagementdb`
  - Username: `sa`
  - Password: `password`

### Sample Data Loaded Automatically:
- 5 Provinces
- 5 Districts
- 4 Sectors
- 5 Cells
- 5 Villages
- 5 Users
- 3 Routes
- 4 UserRoute bookings

---

## API Endpoints Summary

### Provinces
- `GET /api/provinces` - Get all provinces
- `GET /api/provinces/{id}` - Get by ID
- `POST /api/provinces` - Create province
- `DELETE /api/provinces/{id}` - Delete province

### Villages/Locations
- `GET /api/villages` - Get all villages
- `GET /api/villages/{id}` - Get by ID
- `POST /api/villages` - Save village (Location)
- `DELETE /api/villages/{id}` - Delete village

### Users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get by ID
- `POST /api/users/register` - Register user
- `GET /api/users/province/search?provinceCode=01&provinceName=Kigali` - Get by province
- `GET /api/users/district/search?districtCode=...` - Get by district
- `GET /api/users/sector/search?sectorCode=...` - Get by sector
- `GET /api/users/cell/search?cellCode=...` - Get by cell
- `GET /api/users/village/search?villageCode=...` - Get by village
- `GET /api/users/exists/email/{email}` - Check existence by email
- `GET /api/users/exists/phone/{phone}` - Check existence by phone
- `GET /api/users/paginated?page=0&size=10&sortBy=name` - Paginated with sorting
- `GET /api/users/sorted?sortBy=name` - Sorted list

### Routes
- `GET /api/routes` - Get all routes
- `GET /api/routes/{id}` - Get by ID
- `POST /api/routes` - Create route
- `GET /api/routes/paginated?page=0&size=10` - Paginated routes
- `GET /api/routes/sorted?sortBy=routeNumber` - Sorted routes

---

## Examination Checklist ✓

- ✅ ERD with 5+ tables (Province, District, Sector, Cell, Village, User, DriverLicense, Route, UserRoute)
- ✅ Implementation of saving Location (VillageService.saveVillage())
- ✅ Sorting and Pagination (Pageable, Sort in repositories)
- ✅ Many-to-Many relationship (User ↔ Route through UserRoute)
- ✅ One-to-Many relationship (Province → Districts, Village → Users)
- ✅ One-to-One relationship (User ↔ DriverLicense)
- ✅ existsBy() method (existsByEmail, existsByPhone, existsByCode, etc.)
- ✅ Retrieve users by province code OR name (relationship traversal)
- ✅ Comprehensive documentation and explanations

---

**Student:** Patrick DUSHIMIMANA  
**University:** Adventist University of Central Africa  
**Course:** Web Technology and Internet  
**Date:** February 20, 2026
