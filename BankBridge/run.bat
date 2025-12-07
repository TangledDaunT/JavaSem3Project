@echo off
REM BankBridge Run Script for Windows

echo ======================================
echo   Starting BankBridge Application
echo ======================================
echo.

REM Check if compiled
if not exist "out\Main.class" (
    echo Application not compiled. Running compilation first...
    echo.
    call compile.bat
    if %errorlevel% neq 0 (
        echo [X] Compilation failed. Cannot run application.
        pause
        exit /b 1
    )
)

REM Create database directory if not exists
if not exist "database" mkdir database

REM Run the application
java -cp "out;lib\sqlite-jdbc.jar" Main
