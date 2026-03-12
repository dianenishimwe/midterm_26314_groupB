# VIVA-VOCE STUDY GUIDE - BUS MANAGEMENT SYSTEM
## Practical Examination Preparation

---

## QUESTION 1: How do JPA relationships work? (7 Marks Potential)

### Answer:

**What is JPA?**
Java Persistence API (JPA) is a Java specification for object-relational mapping. It allows us to map Java objects to database tables and vice versa.

### Types of Relationships:

**1. One-to-One (@OneToOne)**
- **Example:** User ↔ DriverLicense
- **How it works:** One user has exactly one driver license
- **Implementation:**
  ```java
  // In User entity
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private DriverLicense driverLicense;
  
  // In DriverLicense entity
  @OneToOne
  @JoinColumn(name = "user_id", unique = true)
  private User user;
  ```
- **Foreign Key:** Stored in driver_licenses.user_id
- **Unique constraint:** Ensures one-to-one relationship

**2. One-to-Many / Many-to-One (@OneToMany / @ManyToOne)**
- **Example:** Province → Districts
- **How it works:** One province has many districts, each district belongs to one province
- **Implementation:**
  ```java
  // In Province entity
  @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
  private List<District> districts;
  
  // In District entity
  @ManyToOne
  @JoinColumn(name = "province_id")
  private Province province;
  ```
- **Foreign Key:** Stored in the "many" side (districts.province_id)

**3. Many-to-Many (@ManyToMany or through join entity)**
- **Example:** User ↔ Route through UserRoute
- **How it works:** Many users can book many routes
- **Implementation:**
  ```java
  // Using join entity (better approach)
  @Entity
  public class UserRoute {
      @ManyToOne
      @JoinColumn(name = "user_id")
      private User user;
      
      @ManyToOne
      @JoinColumn(name = "route_id")
      private Route route;
  }
  ```
- **Join Table:** user_routes with user_id and route_id foreign keys

### Key Concepts to Mention:

1. **Owning Side vs Inverse Side:**
   - Owning side has the foreign key
   - Inverse side uses `mappedBy` attribute

2. **Fetch Types:**
   - `FetchType.LAZY`: Load related entities only when accessed (recommended)
   - `FetchType.EAGER`: Load related entities immediately (can cause performance issues)

3. **Cascade Types:**
   - `CascadeType.ALL`: All operations propagate
   - `CascadeType.PERSIST`: Only save operations propagate
   - `CascadeType.REMOVE`: Only delete operations propagate
   - `orphanRemoval = true`: Automatically delete orphaned entities

---

## QUESTION 2: What is the difference between LAZY and EAGER loading?

### Answer:

**LAZY Loading:**
- **Definition:** Related entities are loaded only when explicitly accessed (on-demand)
- **When used:** For collections (@OneToMany, @ManyToMany) by default
- **Advantages:**
  - Better performance
  - Less memory usage
  - Avoids loading unnecessary data
- **Disadvantages:**
  - Can cause LazyInitializationException if accessed outside session
  - Need to be careful with transaction boundaries

**Example:**
```java
@OneToMany(fetch = FetchType.LAZY)
private List<District> districts;

// Districts are NOT loaded when you fetch Province
Province province = provinceRepository.findById(1L).get();

// Districts are loaded ONLY when you access them
for (District district : province.getDistricts()) {
    System.out.println(district.getName());
}
```

**EAGER Loading:**
- **Definition:** Related entities are loaded immediately with the parent entity
- **When used:** For single-valued relationships (@OneToOne, @ManyToOne) by default
- **Advantages:**
  - Convenient - data is always available
  - No LazyInitializationException
- **Disadvantages:**
  - Performance overhead
  - Loads more data than needed
  - Can cause N+1 query problem

**Example:**
```java
@ManyToOne(fetch = FetchType.EAGER)
private Province province;

// Province is loaded automatically when you fetch District
District district = districtRepository.findById(1L).get();
Province province = district.getProvince(); // Already loaded
```

### Best Practice:

**Always prefer LAZY loading** unless you have a specific reason to use EAGER:
```java
// Good practice
@OneToMany(fetch = FetchType.LAZY)
private List<User> users;

@ManyToOne(fetch = FetchType.LAZY)
private Province province;
```

### Performance Comparison:

**Scenario:** Fetching all provinces

**With EAGER loading:**
```sql
SELECT * FROM provinces;              -- 1 query
SELECT * FROM districts WHERE ...     -- 5 queries (one per province)
Total: 6 queries (N+1 problem)
```

**With LAZY loading:**
```sql
SELECT * FROM provinces;              -- 1 query
-- Districts loaded only if accessed
Total: 1 query (much better!)
```

---

## QUESTION 3: How do cascade operations propagate?

### Answer:

**What is Cascade?**
Cascade allows operations on parent entities to automatically propagate to child entities.

### Cascade Types:

**1. CascadeType.ALL**
```java
@OneToMany(cascade = CascadeType.ALL)
private List<District> districts;
```
- Propagates ALL operations (PERSIST, MERGE, REMOVE, REFRESH, DETACH)
- Example: Saving a Province also saves all its Districts

**2. CascadeType.PERSIST**
```java
@OneToMany(cascade = CascadeType.PERSIST)
```
- Only propagates save operations
- Example: Saving Province saves Districts, but deleting Province doesn't delete Districts

**3. CascadeType.REMOVE**
```java
@OneToMany(cascade = CascadeType.REMOVE)
```
- Only propagates delete operations
- Example: Deleting Province deletes all Districts

**4. CascadeType.MERGE**
- Propagates update operations
- Example: Updating Province merges changes to Districts

**5. CascadeType.REFRESH**
- Propagates refresh operations
- Example: Refreshing Province refreshes all Districts from database

### Real Example from Our Project:

```java
// In Province.java
@OneToMany(mappedBy = "province", cascade = CascadeType.ALL, orphanRemoval = true)
private List<District> districts;
```

**What happens when we save:**
```java
Province province = new Province("01", "Kigali City");

District district1 = new District("0101", "Gasabo");
district1.setProvince(province);

District district2 = new District("0102", "Kicukiro");
district2.setProvince(province);

province.setDistricts(List.of(district1, district2));

// Save province - CASCADE saves districts automatically
provinceRepository.save(province);
```

**SQL Generated:**
```sql
-- Insert province
INSERT INTO provinces (code, name) VALUES ('01', 'Kigali City');

-- Insert districts (cascade effect)
INSERT INTO districts (code, name, province_id) VALUES ('0101', 'Gasabo', 1);
INSERT INTO districts (code, name, province_id) VALUES ('0102', 'Kicukiro', 1);
```

### orphanRemoval Explained:

```java
@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
private List<District> districts;
```

**What happens when removing a district:**
```java
province.getDistricts().remove(0); // Remove first district
provinceRepository.save(province); // Orphan removal deletes it from DB
```

**SQL Generated:**
```sql
DELETE FROM districts WHERE id = ?;
```

**Without orphanRemoval:** The district would just have province_id set to NULL

---

## QUESTION 4: How does Spring Data JPA derive queries from method names?

### Answer:

**Spring Data JPA Magic:**
Spring Data JPA automatically generates queries based on method naming patterns - no manual SQL needed!

### Method Name Structure:

**Pattern:** `find|read|get By [Property] [Operator] [Property]`

**Examples:**

1. **Simple Property Match:**
   ```java
   User findByEmail(String email);
   // Generates: SELECT u FROM User u WHERE u.email = :email
   ```

2. **Multiple Conditions (AND):**
   ```java
   User findByNameAndEmail(String name, String email);
   // Generates: SELECT u FROM User u WHERE u.name = ? AND u.email = ?
   ```

3. **OR Condition:**
   ```java
   List<User> findByUserTypeOrVillage(UserType type, Village village);
   // Generates: SELECT u FROM User u WHERE u.userType = ? OR u.village = ?
   ```

4. **Comparison Operators:**
   ```java
   List<User> findByAgeGreaterThan(int age);        // > operator
   List<User> findByAgeLessThan(int age);           // < operator
   List<User> findByAgeBetween(int min, int max);   // BETWEEN operator
   ```

5. **LIKE Operator:**
   ```java
   List<User> findByNameContaining(String keyword);  // LIKE %keyword%
   List<User> findByNameStartingWith(String prefix); // LIKE prefix%
   ```

6. **NULL Check:**
   ```java
   List<User> findByPhoneIsNull();  // WHERE phone IS NULL
   List<User> findByPhoneIsNotNull(); // WHERE phone IS NOT NULL
   ```

7. **Boolean Fields:**
   ```java
   List<User> findByActiveTrue();   // WHERE active = true
   List<User> findByActiveFalse();  // WHERE active = false
   ```

8. **Ordering:**
   ```java
   List<User> findByNameOrderByEmailAsc();  // ORDER BY email ASC
   ```

### Relationship Traversal:

Our project's advanced example:
```java
List<User> findByVillageCellSectorDistrictProvinceCodeOrVillageCellSectorDistrictProvinceName(
    String provinceCode, 
    String provinceName
);
```

**How it traverses relationships:**
```
User.village → Village.cell → Cell.sector → Sector.district → District.province → Province.code/name
```

**Generated JPQL:**
```jpql
SELECT u FROM User u 
WHERE u.village.cell.sector.district.province.code = :provinceCode
   OR u.village.cell.sector.district.province.name = :provinceName
```

### existsBy() Special Case:

```java
boolean existsByEmail(String email);
```

**Generated SQL:**
```sql
SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END 
FROM users u 
WHERE u.email = ?
```

**Why efficient:** Just checks existence, doesn't fetch entire entity

### Query Keywords Reference:

| Keyword | Example | SQL Equivalent |
|---------|---------|----------------|
| And | findByNameAndEmail | WHERE name = ? AND email = ? |
| Or | findByNameOrEmail | WHERE name = ? OR email = ? |
| Is | findByActiveIsTrue | WHERE active = true |
| Between | findByAgeBetween | WHERE age BETWEEN ? AND ? |
| LessThan | findByAgeLessThan | WHERE age < ? |
| GreaterThan | findByAgeGreaterThan | WHERE age > ? |
| Like | findByNameLike | WHERE name LIKE ? |
| Containing | findByNameContaining | WHERE name LIKE %?% |
| OrderBy | findByNameOrderByAgeDesc | ORDER BY age DESC |

---

## QUESTION 5: What are the performance benefits of pagination? (5 Marks)

### Answer:

**What is Pagination?**
Pagination divides large datasets into smaller chunks (pages) that can be loaded and processed separately.

### Performance Benefits:

**1. Reduced Memory Usage**

**Without Pagination:**
```java
List<User> allUsers = userRepository.findAll();
// If table has 100,000 users:
// - Loads 100,000 objects into memory
// - Might cause OutOfMemoryError
// - High garbage collection pressure
```

**With Pagination:**
```java
Page<User> userPage = userRepository.findAll(PageRequest.of(0, 10));
// Only loads 10 users at a time
// - Memory usage: 99.99% less!
// - No risk of OutOfMemoryError
// - Minimal GC pressure
```

**Memory Calculation:**
- Assume 1 User object = 1 KB
- 100,000 users = 100 MB without pagination
- 10 users per page = 10 KB per page
- **Savings: 99.99 MB (99.99%)**

**2. Improved Response Time**

**Without Pagination:**
```
Database fetch: 500ms (100,000 records)
JSON serialization: 300ms
Network transfer: 200ms
Total: 1000ms (1 second)
```

**With Pagination (10 items per page):**
```
Database fetch: 5ms (10 records)
JSON serialization: 3ms
Network transfer: 2ms
Total: 10ms (0.01 seconds)
```

**Improvement: 100x faster!**

**3. Better User Experience**

**Without Pagination:**
- User waits 1 second to see first result
- Page appears frozen during load
- Poor perceived performance

**With Pagination:**
- First page loads in 10ms (instant!)
- User can start interacting immediately
- Additional pages load on demand
- Smooth, responsive UI

**4. Database Optimization**

**Query Performance:**
```sql
-- Without pagination
SELECT * FROM users;
-- Full table scan
-- Sorts 100,000 rows
-- Returns 100,000 rows

-- With pagination
SELECT * FROM users ORDER BY name LIMIT 10 OFFSET 0;
-- Uses index on 'name'
-- Sorts only until 10 rows found
-- Returns only 10 rows
```

**Lock Contention:**
- Shorter queries = shorter locks
- Less blocking of other transactions
- Higher concurrency

**5. Network Efficiency**

**Bandwidth Savings:**
```
Without pagination: 100,000 users × 1KB = 100MB transferred
With pagination: 10 users × 1KB = 10KB per page
Savings: 99.99% less bandwidth
```

**6. Scalability**

**Handles Growth:**
- Works efficiently even with millions of records
- Response time stays constant regardless of total records
- System can scale to more users

### Implementation Details:

**How Spring Data JPA Implements Pagination:**

```java
public Page<User> getAllUsersPaginated(int page, int size, String sortBy, boolean ascending) {
    Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return userRepository.findAll(pageable);
}
```

**Two Queries Executed:**
```sql
-- 1. COUNT query (to get total pages)
SELECT COUNT(*) FROM users;

-- 2. DATA query (with LIMIT and OFFSET)
SELECT * FROM users ORDER BY name ASC LIMIT 10 OFFSET 0;
```

### Real-World Example:

**Scenario:** E-commerce site with 1 million products

**Without Pagination:**
- Homepage takes 30 seconds to load
- Server crashes under load
- Users abandon site

**With Pagination (20 items per page):**
- Homepage loads in 50ms
- Server handles 1000 concurrent users
- Users browse smoothly
- **Result: 600x faster homepage!**

### When to Use Pagination:

✅ **Use Pagination When:**
- Displaying lists in UI
- Working with large datasets (>100 records)
- Building REST APIs
- Exporting data in batches

❌ **Don't Use When:**
- Generating reports that need all data
- Small, fixed-size datasets
- Background batch processing

---

## QUESTION 6: Explain foreign key constraints and referential integrity

### Answer:

**What is a Foreign Key?**
A foreign key is a field (or collection of fields) in one table that refers to the primary key in another table.

**Purpose:**
- Creates relationship between tables
- Enforces referential integrity
- Prevents invalid data

### Foreign Key Examples from Our Project:

**1. Many-to-One (villages → cells):**
```sql
CREATE TABLE villages (
    id BIGINT PRIMARY KEY,
    cell_id BIGINT NOT NULL,
    FOREIGN KEY (cell_id) REFERENCES cells(id)
);
```

**What it enforces:**
- Every village MUST belong to a valid cell
- Cannot insert village with non-existent cell_id
- Cannot delete a cell if it has villages

**2. One-to-One (driver_licenses → users):**
```sql
CREATE TABLE driver_licenses (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

**What it enforces:**
- Every license MUST belong to a valid user
- UNIQUE ensures one license per user
- Cannot delete a user if they have a license (unless CASCADE)

### Referential Integrity Rules:

**Rule 1: Parent Must Exist**
```sql
-- This will FAIL (cell_id = 999 doesn't exist)
INSERT INTO villages (id, cell_id, name, code) 
VALUES (1, 999, 'Test Village', 'TV001');

-- This will SUCCEED (cell_id = 1 exists)
INSERT INTO villages (id, cell_id, name, code) 
VALUES (1, 1, 'Test Village', 'TV001');
```

**Rule 2: Cannot Delete Parent with Children**
```sql
-- This will FAIL if cell has villages
DELETE FROM cells WHERE id = 1;
```

**Solution:** Use CASCADE options

### CASCADE Options:

**1. CASCADE DELETE:**
```sql
CREATE TABLE driver_licenses (
    user_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

**Behavior:**
```sql
-- Delete user - license is automatically deleted
DELETE FROM users WHERE id = 1;
-- driver_licenses with user_id = 1 are automatically deleted
```

**2. SET NULL:**
```sql
CREATE TABLE villages (
    cell_id BIGINT,
    FOREIGN KEY (cell_id) REFERENCES cells(id) ON DELETE SET NULL
);
```

**Behavior:**
```sql
-- Delete cell - villages' cell_id set to NULL
DELETE FROM cells WHERE id = 1;
-- villages.cell_id = NULL for affected villages
```

**3. RESTRICT (Default):**
```sql
CREATE TABLE villages (
    cell_id BIGINT NOT NULL,
    FOREIGN KEY (cell_id) REFERENCES cells(id) ON DELETE RESTRICT
);
```

**Behavior:**
```sql
-- FAILS if cell has villages
DELETE FROM cells WHERE id = 1;
-- Error: Cannot delete parent with children
```

### In JPA/Hibernate:

**CascadeType.REMOVE:**
```java
@OneToMany(cascade = CascadeType.REMOVE)
private List<District> districts;
```

**Equivalent to SQL CASCADE DELETE**

**orphanRemoval:**
```java
@OneToMany(orphanRemoval = true)
private List<District> districts;
```

**Behavior:**
```java
province.getDistricts().remove(district);
// Hibernate executes: DELETE FROM districts WHERE id = ?
```

### Why Referential Integrity Matters:

**1. Data Consistency:**
- Prevents orphaned records
- Ensures relationships are valid
- Maintains database accuracy

**2. Application Logic:**
- Reduces validation code
- Database enforces rules automatically
- Fewer bugs

**3. Performance:**
- Indexed foreign keys improve JOIN performance
- Query optimizer uses constraints for better plans

### Common Errors:

**IntegrityConstraintViolationException:**
```java
// Trying to delete parent with children
provinceRepository.delete(province);
// Throws: IntegrityConstraintViolationException
// Solution: Delete children first or use cascade
```

**PropertyValueException:**
```java
// Trying to save child with non-existent parent
village.setCell(null);
villageRepository.save(village);
// Throws: PropertyValueException (NOT NULL constraint)
```

---

## QUESTION 7: Explain the Rwanda administrative structure implementation

### Answer:

**Rwanda Administrative Hierarchy:**
```
1. Province (5 provinces)
   ↓
2. District (30 districts)
   ↓
3. Sector (416 sectors)
   ↓
4. Cell (2,148 cells)
   ↓
5. Village (14,837 villages)
   ↓
6. Users (linked to villages)
```

### Why This Design?

**1. Hierarchical Structure:**
- Each level has clear parent-child relationship
- Mirrors real-world administrative divisions
- Easy to understand and query

**2. Users Linked to Village Only:**
```java
@Entity
public class User {
    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;
}
```

**Benefits:**
- **Single Point of Truth:** User's location defined once
- **Automatic Inheritance:** User belongs to all parent levels automatically
- **Flexibility:** Can query users by ANY administrative level
- **Data Integrity:** No conflicting assignments

### Querying Users by Administrative Level:

**By Province:**
```java
List<User> findByVillageCellSectorDistrictProvinceCode(String code);
// Traverses: User → Village → Cell → Sector → District → Province
```

**By District:**
```java
List<User> findByVillageCellSectorDistrictCode(String code);
// Traverses: User → Village → Cell → Sector → District
```

**By Village:**
```java
List<User> findByVillageCode(String code);
// Direct relationship - fastest query
```

### Sample Data Structure:

```
Province: Kigali City (code: "01")
  └─ District: Gasabo (code: "0101")
      └─ Sector: Gatsata (code: "010101")
          └─ Cell: Gatsata (code: "01010101")
              └─ Village: Rebero (code: "0101010101")
                  └─ Users: [Patrick, Marie, ...]
```

### Code Assignment Pattern:

**Hierarchical Coding:**
```
Province: 01
District: 01 + 01 = 0101
Sector:   0101 + 01 = 010101
Cell:     010101 + 01 = 01010101
Village:  01010101 + 01 = 0101010101
```

**Benefits:**
- Can determine hierarchy from code
- Easy to validate
- Supports efficient indexing

### Performance Considerations:

**Indexing Strategy:**
```sql
CREATE INDEX idx_village_cell ON villages(cell_id);
CREATE INDEX idx_cell_sector ON cells(sector_id);
CREATE INDEX idx_sector_district ON sectors(district_id);
CREATE INDEX idx_district_province ON districts(province_id);
CREATE INDEX idx_user_village ON users(village_id);
```

**Query Optimization:**
```sql
-- Efficient query with proper indexes
SELECT u.* 
FROM users u
JOIN villages v ON u.village_id = v.id
JOIN cells c ON v.cell_id = c.id
JOIN sectors s ON c.sector_id = s.id
JOIN districts d ON s.district_id = d.id
JOIN provinces p ON d.province_id = p.id
WHERE p.code = '01';
```

### Business Use Cases:

**1. Reporting:**
- Count users per province
- Analyze route popularity by district
- Resource allocation based on population

**2. Access Control:**
- Admin manages users in their province only
- Regional managers for each province

**3. Service Delivery:**
- Assign routes based on geographic area
- Target services to specific regions

---

## GENERAL TIPS FOR VIVA-VOCE:

### 1. Speak Confidently About What You Implemented
- Use "I implemented" not "The code has"
- Show ownership of your work

### 2. Explain Trade-offs
- "I chose LAZY loading because..."
- "Using join entity instead of @ManyToMany allows..."

### 3. Demonstrate Understanding
- Don't just recite definitions
- Give examples from your project
- Show how concepts connect

### 4. Be Honest About Limitations
- "I didn't implement caching, but I would use Redis for..."
- "For production, I would add validation for..."

### 5. Prepare Questions to Ask
- "Would you like me to demonstrate the pagination endpoint?"
- "Should I show you the ERD diagram?"

---

## COMMON FOLLOW-UP QUESTIONS:

**Q: What if you need to change a user's village?**
```java
User user = userRepository.findById(1L).get();
Village newVillage = villageRepository.findById(2L).get();
user.setVillage(newVillage);
userRepository.save(user);
// Simple! JPA updates the foreign key automatically
```

**Q: How would you handle concurrent bookings for the same seat?**
```java
@Transactional
public UserRoute bookSeat(Long userId, Long routeId, String seatNumber) {
    // Check if seat already booked
    if (userRouteRepository.existsByRouteAndSeatNumber(routeId, seatNumber)) {
        throw new SeatAlreadyBookedException();
    }
    
    // Create booking
    UserRoute booking = new UserRoute();
    booking.setUser(userRepository.findById(userId).get());
    booking.setRoute(routeRepository.findById(routeId).get());
    booking.setSeatNumber(seatNumber);
    booking.setBookingDate(LocalDateTime.now());
    booking.setStatus(BookingStatus.CONFIRMED);
    
    return userRouteRepository.save(booking);
}
// Transaction ensures atomicity - either both succeed or both fail
```

**Q: How would you optimize queries for large datasets?**
- Add indexes on frequently queried columns
- Use DTO projections instead of full entities
- Implement caching (Redis, Ehcache)
- Use database views for complex joins
- Consider read replicas for scaling reads

---

**Good luck with your examination!**
**Remember: Confidence + Preparation = Success**

*Prepared by: Patrick DUSHIMIMANA*
*Date: February 20, 2026*
