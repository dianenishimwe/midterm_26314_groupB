# ✅ SPRING BOOT STARTUP VERIFICATION CHECKLIST

## 🔍 What to Look For in the Console

### 1. Spring Boot Banner (First Thing You'll See)
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
```
✅ If you see this, Spring Boot is loading!

---

### 2. Database Connection Messages
Look for:
```
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
```
✅ Database connected successfully!

---

### 3. JPA/Hibernate Table Creation
You should see SQL statements like:
```
Hibernate: create table users (...)
Hibernate: create table provinces (...)
Hibernate: create table buses (...)
```
✅ Tables are being created!

---

### 4. Data Loading (If DataLoader runs)
Look for:
```
Data loading completed successfully!

=== Summary ===
Provinces: 5
Districts: 5
Sectors: 4
Cells: 5
Villages: 5
Users: 5
Routes: 3
```
✅ Sample data loaded!

---

### 5. Application Started Message (MOST IMPORTANT!)
```
Started BusmanagementApplication in X.XXX seconds (JVM running for X.XXX)
```
✅ **APPLICATION IS READY!** You can now use Postman!

---

### 6. Port Information
Look for:
```
Tomcat started on port(s): 8080 (http)
```
✅ Server is listening on port 8080!

---

## ⚠️ COMMON ERRORS & SOLUTIONS

### ❌ Error: "Port 8080 is already in use"
**Message:**
```
Web server failed to start. Port 8080 was already in use.
```
**Solution:**
1. Close any other applications using port 8080
2. Or change port in application.properties: `server.port=8081`

---

### ❌ Error: "Failed to configure a DataSource"
**Message:**
```
Failed to configure a DataSource: 'url' attribute is not specified
```
**Solution:**
- Make sure you're using H2 profile: `--spring.profiles.active=h2`
- Or check PostgreSQL is running if using PostgreSQL

---

### ❌ Error: "Connection refused: localhost:5432"
**Message:**
```
Connection to localhost:5432 refused
```
**Solution:**
- PostgreSQL is not running
- Use H2 instead: Run with `--spring.profiles.active=h2`

---

### ❌ Error: "ClassNotFoundException"
**Message:**
```
java.lang.ClassNotFoundException: com.bus.busmanagement.model.Bus
```
**Solution:**
- Recompile the project: `mvnw.cmd clean package -DskipTests`

---

## 🎯 ONCE YOU SEE "Started BusmanagementApplication"

### Immediately Test These in Postman:

**1. Health Check - Get All Users**
```
GET http://localhost:8080/api/users
```
Expected: List of users (might be empty if no data loaded)

**2. Get All Provinces**
```
GET http://localhost:8080/api/provinces
```
Expected: List of 5 provinces

**3. Create Your First User**
```
POST http://localhost:8080/api/users/register
Content-Type: application/json

{
    "name": "Patrick DUSHIMIMANA",
    "email": "patrick@example.com",
    "phone": "0788123456",
    "userType": "PASSENGER",
    "villageId": 1
}
```
Expected: User object with ID

---

## 📝 STARTUP LOG EXAMPLE (What Success Looks Like)

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

2024-03-11 10:30:15.123  INFO --- [main] c.b.b.BusmanagementApplication : Starting BusmanagementApplication
2024-03-11 10:30:16.456  INFO --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2024-03-11 10:30:17.789  INFO --- [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2024-03-11 10:30:18.012  INFO --- [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2024-03-11 10:30:19.345  INFO --- [main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000490: Using JtaPlatform implementation
2024-03-11 10:30:20.678  INFO --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http)
2024-03-11 10:30:21.901  INFO --- [main] c.b.b.BusmanagementApplication : Started BusmanagementApplication in 6.789 seconds
```

✅ **THIS MEANS SUCCESS!**

---

## 🚀 NEXT STEPS

1. ✅ Keep the console window open (don't close it!)
2. ✅ Open Postman
3. ✅ Test the endpoints from POSTMAN_TESTING_GUIDE.md
4. ✅ Start creating users!

---

## 🛑 TO STOP THE APPLICATION

- Press `Ctrl + C` in the console window
- Or just close the window

---

**Your application is starting now! Watch for "Started BusmanagementApplication" message!** 🎉
