@echo off
echo ========================================
echo RESTARTING BUS MANAGEMENT SYSTEM
echo ========================================
echo.

cd /d "%~dp0"

echo Step 1: Stopping any running instances...
taskkill /F /IM java.exe 2>nul
timeout /t 2 >nul

echo.
echo Step 2: Recompiling application...
call mvnw.cmd clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================
    echo COMPILATION FAILED!
    echo ========================================
    pause
    exit /b 1
)

echo.
echo ========================================
echo COMPILATION SUCCESS!
echo ========================================
echo.
echo Step 3: Starting application...
echo.
echo Application will be available at: http://localhost:8080
echo.
echo IMPORTANT: Watch for "Started BusmanagementApplication" message
echo Then test in Postman!
echo.
echo Press Ctrl+C to stop the application
echo.

java -jar target\busmanagement-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2

pause
