# 🔧 TROUBLESHOOTING GUIDE

## If "Nothing Changes" When Starting Application

### ✅ SOLUTION: Use the Restart Script

1. **Close the current window** (if application is running)
2. **Double-click:** `restart-app.bat`
3. **Wait for:** "Started BusmanagementApplication"
4. **Test in Postman**

---

## Common Issues & Fixes

### Issue 1: Application Starts But Postman Still Shows Connection Refused

**Symptoms:**
- Console shows "Started BusmanagementApplication"
- Postman still shows "ECONNREFUSED"

**Solutions:**
1. Check the port number in console output
2. Make sure Postman URL is: `http://localhost:8080` (not https)
3. Try: `http://127.0.0.1:8080/api/users`
4. Restart Postman

---

### Issue 2: Port 8080 Already in Use

**Symptoms:**
```
Web server failed to start. Port 8080 was already in use.
```

**Solutions:**

**Option A: Kill the process using port 8080**
```cmd
netstat -ano | findstr :8080
taskkill /F /PID <PID_NUMBER>
```

**Option B: Use a different port**
1. Open `application.properties`
2. Add: `server.port=8081`
3. Restart application
4. Use `http://localhost:8081` in Postman

---

### Issue 3: PostgreSQL Connection Error

**Symptoms:**
```
Connection to localhost:5432 refused
```

**Solution:**
Use H2 database instead (no installation needed):
```cmd
java -jar target\busmanagement-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2
```

Or use the `restart-app.bat` script (it uses H2 automatically)

---

### Issue 4: Compilation Errors

**Symptoms:**
```
BUILD FAILURE
[ERROR] compilation failed
```

**Solution:**
1. Close all IDE windows
2. Delete `target` folder
3. Run: `mvnw.cmd clean package -DskipTests`

---

### Issue 5: Application Starts But No Data

**Symptoms:**
- Application starts successfully
- GET /api/users returns empty array `[]`

**Solution:**
This is normal! DataLoader might not have run. Just create users manually:

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

### Issue 6: "Village not found" Error

**Symptoms:**
```
{
    "error": "Village not found with id: 1"
}
```

**Solution:**
Villages might not be loaded. First create a province structure:

**Step 1: Create Province**
```
POST http://localhost:8080/api/provinces
Content-Type: application/json

{
    "code": "01",
    "name": "Kigali City"
}
```

**Step 2: Then create user** (after villages are loaded)

---

## 🎯 QUICK FIX - START FRESH

If nothing works, do this:

1. **Stop everything:**
   - Close all command prompts
   - Close IDE
   - Kill Java processes: `taskkill /F /IM java.exe`

2. **Clean build:**
   ```cmd
   cd c:\busmanagementmidterm\busmanagement\busmanagement
   mvnw.cmd clean
   ```

3. **Rebuild:**
   ```cmd
   mvnw.cmd package -DskipTests
   ```

4. **Start fresh:**
   ```cmd
   java -jar target\busmanagement-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2
   ```

5. **Test in Postman:**
   ```
   GET http://localhost:8080/api/users
   ```

---

## 📞 VERIFICATION STEPS

### Step 1: Check if Java is running
```cmd
tasklist | findstr java
```
Should show: `java.exe`

### Step 2: Check if port 8080 is listening
```cmd
netstat -ano | findstr :8080
```
Should show: `0.0.0.0:8080` or `127.0.0.1:8080`

### Step 3: Test with browser
Open: `http://localhost:8080/api/users`
Should show: JSON response (even if empty array)

### Step 4: Test with curl (if available)
```cmd
curl http://localhost:8080/api/users
```

---

## ✅ SUCCESS INDICATORS

You'll know it's working when:

1. ✅ Console shows: "Started BusmanagementApplication"
2. ✅ Console shows: "Tomcat started on port(s): 8080"
3. ✅ Browser shows JSON at: http://localhost:8080/api/users
4. ✅ Postman GET request returns 200 OK

---

## 🆘 STILL NOT WORKING?

### Try the Absolute Simplest Test:

1. Make sure application is running
2. Open browser (Chrome/Firefox)
3. Go to: `http://localhost:8080/api/users`
4. You should see JSON (even if empty: `[]`)

If browser works but Postman doesn't:
- Restart Postman
- Check Postman proxy settings (disable if enabled)
- Try Postman web version: https://web.postman.co

---

## 📝 WHAT TO DO NOW

1. **Close current window** (if app is running)
2. **Double-click:** `restart-app.bat`
3. **Wait for:** "Started BusmanagementApplication in X.XXX seconds"
4. **Open browser:** http://localhost:8080/api/users
5. **If browser works:** Open Postman and test
6. **If browser doesn't work:** Check this guide again

---

**The restart-app.bat script will fix most issues!** 🚀
