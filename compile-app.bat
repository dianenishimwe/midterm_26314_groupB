@echo off
echo ========================================
echo Compiling Bus Management System
echo ========================================
echo.

cd /d "%~dp0"

echo Running Maven clean package...
echo This may take a few minutes...
echo.

call mvnw.cmd clean package -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo BUILD SUCCESS!
    echo ========================================
    echo.
    echo You can now run: start-app.bat
    echo.
) else (
    echo.
    echo ========================================
    echo BUILD FAILED!
    echo ========================================
    echo Please check the errors above
    echo.
)

pause
