# POSTMAN TESTING GUIDE - BUS MANAGEMENT SYSTEM

## BASE URL
```
http://localhost:8080
```

---

## 1. CREATE USER (Register New User)

### Endpoint
```
POST http://localhost:8080/api/users/register
```

### Headers
```
Content-Type: application/json
```

### Request Body (JSON)
```json
{
    "name": "Patrick DUSHIMIMANA",
    "email": "patrick@example.com",
    "phone": "0788123456",
    "userType": "PASSENGER",
    "villageId": 1
}
```

### Expected Response (200 OK)
```json
{
    "id": 6,
    "name": "Patrick DUSHIMIMANA",
    "email": "patrick@example.com",
    "phone": "0788123456",
    "userType": "PASSENGER",
    "village": {
        "id": 1,
        "code": "0101010101",
        "name": "Rebero"
    }
}
```

---

## 2. GET ALL USERS

### Endpoint
```
GET http://localhost:8080/api/users
```

### Expected Response (200 OK)
```json
[
    {
        "id": 1,
        "name": "Diane NISHIMWE",
        "email": "dianenishimwe@gmail.com",
        "phone": "0788888888",
        "userType": "PASSENGER"
    },
    ...
]
```

---

## 3. GET USER BY ID

### Endpoint
```
GET http://localhost:8080/api/users/1
```

### Expected Response (200 OK)
```json
{
    "id": 1,
    "name": "Diane NISHIMWE",
    "email": "dianenishimwe@gmail.com",
    "phone": "0788888888",
    "userType": "PASSENGER"
}
```

---

## 4. CHECK IF EMAIL EXISTS (existsBy Method)

### Endpoint
```
GET http://localhost:8080/api/users/exists/email/patrick@example.com
```

### Expected Response (200 OK)
```json
{
    "exists": true
}
```

---

## 5. GET USERS BY PROVINCE CODE

### Endpoint
```
GET http://localhost:8080/api/users/province/search?provinceCode=01
```

### Expected Response (200 OK)
```json
[
    {
        "id": 1,
        "name": "Diane NISHIMWE",
        "email": "dianenishimwe@gmail.com",
        "userType": "PASSENGER"
    },
    ...
]
```

---

## 6. GET USERS BY PROVINCE NAME

### Endpoint
```
GET http://localhost:8080/api/users/province/search?provinceName=Kigali City
```

---

## 7. GET USERS WITH PAGINATION

### Endpoint
```
GET http://localhost:8080/api/users/paginated?page=0&size=2&sortBy=name&ascending=true
```

### Expected Response (200 OK)
```json
{
    "content": [
        {
            "id": 1,
            "name": "Diane NISHIMWE",
            "email": "dianenishimwe@gmail.com"
        },
        {
            "id": 2,
            "name": "Jean MUGABO",
            "email": "jeanmugabo@gmail.com"
        }
    ],
    "totalElements": 5,
    "totalPages": 3,
    "number": 0,
    "size": 2,
    "first": true,
    "last": false
}
```

---

## 8. CREATE BUS

### Endpoint
```
POST http://localhost:8080/api/buses
```

### Headers
```
Content-Type: application/json
```

### Request Body (JSON)
```json
{
    "plateNumber": "RAD 123 A",
    "model": "Volvo B11R",
    "capacity": 50,
    "status": "ACTIVE",
    "routeId": 1
}
```

### Expected Response (200 OK)
```json
{
    "id": 1,
    "plateNumber": "RAD 123 A",
    "model": "Volvo B11R",
    "capacity": 50,
    "status": "ACTIVE"
}
```

---

## 9. GET ALL BUSES

### Endpoint
```
GET http://localhost:8080/api/buses
```

---

## 10. GET BUSES BY STATUS

### Endpoint
```
GET http://localhost:8080/api/buses/status/ACTIVE
```

---

## 11. GET ALL ROUTES

### Endpoint
```
GET http://localhost:8080/api/routes
```

---

## 12. GET ALL PROVINCES

### Endpoint
```
GET http://localhost:8080/api/provinces
```

---

## POSTMAN SETUP INSTRUCTIONS

### Step 1: Open Postman
- Launch Postman application

### Step 2: Create New Request
1. Click "New" → "HTTP Request"
2. Select method (GET, POST, etc.)
3. Enter URL

### Step 3: For POST Requests
1. Select "Body" tab
2. Choose "raw"
3. Select "JSON" from dropdown
4. Paste JSON data

### Step 4: Send Request
- Click "Send" button
- View response below

---

## TESTING ORDER (RECOMMENDED)

1. ✅ GET /api/provinces - Verify data loaded
2. ✅ GET /api/users - Check existing users
3. ✅ POST /api/users/register - Create new user
4. ✅ GET /api/users/exists/email/{email} - Test existsBy()
5. ✅ GET /api/users/province/search?provinceCode=01 - Test province query
6. ✅ GET /api/users/paginated?page=0&size=2 - Test pagination
7. ✅ POST /api/buses - Create new bus
8. ✅ GET /api/buses - View all buses

---

## COMMON ERRORS & SOLUTIONS

### Error: Connection Refused
**Solution:** Make sure Spring Boot application is running

### Error: 404 Not Found
**Solution:** Check URL spelling and port number (8080)

### Error: 400 Bad Request
**Solution:** Check JSON format and required fields

### Error: 500 Internal Server Error
**Solution:** Check villageId exists in database (use 1, 2, 3, 4, or 5)

---

## SAMPLE USER DATA FOR TESTING

```json
{
    "name": "Alice MUKAMANA",
    "email": "alice@example.com",
    "phone": "0788111111",
    "userType": "PASSENGER",
    "villageId": 1
}
```

```json
{
    "name": "Bob NKURUNZIZA",
    "email": "bob@example.com",
    "phone": "0788222222",
    "userType": "DRIVER",
    "villageId": 2
}
```

```json
{
    "name": "Carol UWASE",
    "email": "carol@example.com",
    "phone": "0788333333",
    "userType": "ADMIN",
    "villageId": 3
}
```

---

## EXAMINATION DEMONSTRATION CHECKLIST

- [ ] Create a new user (POST /api/users/register)
- [ ] Get all users (GET /api/users)
- [ ] Check email exists (GET /api/users/exists/email/{email})
- [ ] Get users by province (GET /api/users/province/search?provinceCode=01)
- [ ] Test pagination (GET /api/users/paginated?page=0&size=2)
- [ ] Create a bus (POST /api/buses)
- [ ] Get buses by route (GET /api/buses/route/1)

---

**Ready to test in Postman!**
