@echo off
echo ========================================
echo Starting Bus Management System
echo ========================================
echo.

cd /d "%~dp0"

echo Checking if application is already compiled...
if not exist "target\busmanagement-0.0.1-SNAPSHOT.jar" (
    echo JAR file not found. Please compile first with: mvnw.cmd clean package
    pause
    exit /b 1
)

echo Starting Spring Boot Application...
echo.
echo Application will be available at: http://localhost:8080
echo H2 Console available at: http://localhost:8080/h2-console
echo.
echo Press Ctrl+C to stop the application
echo.

java -jar target\busmanagement-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2

pause
