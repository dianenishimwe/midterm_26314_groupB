@echo off
echo ========================================
echo STARTING BUS MANAGEMENT SYSTEM
echo ========================================
echo.

echo Current Directory:
cd
echo.

echo Step 1: Cleaning and compiling...
echo This may take 2-3 minutes...
echo.

call mvnw.cmd clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================
    echo COMPILATION FAILED!
    echo ========================================
    echo Please check the errors above
    pause
    exit /b 1
)

echo.
echo ========================================
echo COMPILATION SUCCESS!
echo ========================================
echo.

echo Step 2: Starting Spring Boot Application...
echo.
echo Watch for: "Started BusmanagementApplication"
echo.
echo Application URL: http://localhost:8080
echo H2 Console: http://localhost:8080/h2-console
echo.
echo Press Ctrl+C to stop
echo.

java -jar target\busmanagement-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2

pause
