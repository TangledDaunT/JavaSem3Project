@echo off
REM BankBridge Compilation Script for Windows

echo ======================================
echo   BankBridge Compilation Script
echo ======================================
echo.

REM Check if Java is installed
javac -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [X] Error: javac not found. Please install JDK.
    pause
    exit /b 1
)

echo [+] Java compiler found
java -version
echo.

REM Check if SQLite JDBC driver exists
if not exist "lib\sqlite-jdbc.jar" (
    echo [X] Error: sqlite-jdbc.jar not found in lib\ directory
    echo Please download it from: https://github.com/xerial/sqlite-jdbc/releases
    pause
    exit /b 1
)

echo [+] SQLite JDBC driver found
echo.

REM Create output directory
echo Creating output directory...
if not exist "out" mkdir out
echo [+] Output directory ready
echo.

REM Compile Java files
echo Compiling Java source files...
echo --------------------------------------

echo [1/6] Compiling exceptions...
javac -cp ".;lib\sqlite-jdbc.jar" -d out src\exceptions\*.java
if %errorlevel% neq 0 (
    echo [X] Compilation failed for exceptions
    pause
    exit /b 1
)

echo [2/6] Compiling model...
javac -cp ".;lib\sqlite-jdbc.jar;out" -d out src\model\*.java
if %errorlevel% neq 0 (
    echo [X] Compilation failed for model
    pause
    exit /b 1
)

echo [3/6] Compiling db...
javac -cp ".;lib\sqlite-jdbc.jar;out" -d out src\db\*.java
if %errorlevel% neq 0 (
    echo [X] Compilation failed for db
    pause
    exit /b 1
)

echo [4/6] Compiling threads...
javac -cp ".;lib\sqlite-jdbc.jar;out" -d out src\threads\*.java
if %errorlevel% neq 0 (
    echo [X] Compilation failed for threads
    pause
    exit /b 1
)

echo [5/6] Compiling gui...
javac -cp ".;lib\sqlite-jdbc.jar;out" -d out src\gui\*.java
if %errorlevel% neq 0 (
    echo [X] Compilation failed for gui
    pause
    exit /b 1
)

echo [6/6] Compiling Main...
javac -cp ".;lib\sqlite-jdbc.jar;out" -d out src\Main.java
if %errorlevel% neq 0 (
    echo [X] Compilation failed for Main
    pause
    exit /b 1
)

echo.
echo ======================================
echo [+] Compilation successful!
echo ======================================
echo.
echo To run the application, execute:
echo   run.bat
echo or
echo   java -cp "out;lib\sqlite-jdbc.jar" Main
echo.
pause
