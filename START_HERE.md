# 🚀 QUICK START GUIDE

## Option 1: Using Batch Scripts (EASIEST)

### Step 1: Compile the Application
1. Double-click `compile-app.bat`
2. Wait for "BUILD SUCCESS" message
3. Close the window

### Step 2: Start the Application
1. Double-click `start-app.bat`
2. Wait for "Started BusmanagementApplication" message
3. Keep this window open (don't close it!)

### Step 3: Test in Postman
- Application is now running at: `http://localhost:8080`
- Start testing your endpoints!

---

## Option 2: Using Command Prompt

### Step 1: Open Command Prompt
1. Press `Win + R`
2. Type `cmd` and press Enter
3. Navigate to project folder:
```cmd
cd c:\busmanagementmidterm\busmanagement\busmanagement
```

### Step 2: Compile (if needed)
```cmd
mvnw.cmd clean package -DskipTests
```

### Step 3: Run the Application
```cmd
java -jar target\busmanagement-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2
```

---

## Option 3: Using Your IDE (IntelliJ/Eclipse)

### IntelliJ IDEA:
1. Open the project in IntelliJ
2. Find `BusmanagementApplication.java`
3. Right-click → Run 'BusmanagementApplication'

### Eclipse:
1. Open the project in Eclipse
2. Find `BusmanagementApplication.java`
3. Right-click → Run As → Java Application

---

## ✅ How to Know It's Running

You should see output like:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

Started BusmanagementApplication in 5.234 seconds
```

---

## 🧪 Test Endpoints in Postman

Once running, test these:

### 1. Check if server is alive
```
GET http://localhost:8080/api/users
```

### 2. Create a user
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

---

## ⚠️ Troubleshooting

### Error: "Port 8080 already in use"
**Solution:** Another application is using port 8080
- Close other applications
- Or change port in `application.properties`: `server.port=8081`

### Error: "Cannot find mvnw.cmd"
**Solution:** You're in the wrong directory
- Make sure you're in: `c:\busmanagementmidterm\busmanagement\busmanagement`

### Error: "Java not found"
**Solution:** Java is not installed or not in PATH
- Install Java 17 or higher
- Or use your IDE to run the application

### Error: "PostgreSQL connection refused"
**Solution:** Use H2 database instead
- Run with: `--spring.profiles.active=h2`
- Or use the `start-app.bat` script

---

## 🎯 Quick Commands Reference

**Compile:**
```cmd
mvnw.cmd clean package -DskipTests
```

**Run with H2 (no PostgreSQL needed):**
```cmd
java -jar target\busmanagement-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2
```

**Run with PostgreSQL:**
```cmd
java -jar target\busmanagement-0.0.1-SNAPSHOT.jar
```

**Stop the application:**
- Press `Ctrl + C` in the command prompt window

---

## 📊 Database Access

### H2 Console (when using H2):
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:busmanagementdb`
- Username: `sa`
- Password: `password`

---

**Now you're ready to test in Postman! 🎉**
