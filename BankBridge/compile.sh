#!/bin/bash

# BankBridge Compilation Script
# This script compiles all Java source files

echo "======================================"
echo "  BankBridge Compilation Script"
echo "======================================"
echo ""

# Check if Java is installed
if ! command -v javac &> /dev/null; then
    echo "❌ Error: javac not found. Please install JDK."
    exit 1
fi

echo "✓ Java compiler found"
java -version
echo ""

# Check if SQLite JDBC driver exists
if [ ! -f "lib/sqlite-jdbc.jar" ]; then
    echo "❌ Error: sqlite-jdbc.jar not found in lib/ directory"
    echo "Please download it from: https://github.com/xerial/sqlite-jdbc/releases"
    exit 1
fi

echo "✓ SQLite JDBC driver found"
echo ""

# Create output directory
echo "Creating output directory..."
mkdir -p out
echo "✓ Output directory ready"
echo ""

# Compile Java files
echo "Compiling Java source files..."
echo "--------------------------------------"

# Compile in order of dependencies
echo "[1/6] Compiling exceptions..."
javac -cp ".:lib/sqlite-jdbc.jar" -d out src/exceptions/*.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for exceptions"
    exit 1
fi

echo "[2/6] Compiling model..."
javac -cp ".:lib/sqlite-jdbc.jar:out" -d out src/model/*.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for model"
    exit 1
fi

echo "[3/6] Compiling db..."
javac -cp ".:lib/sqlite-jdbc.jar:out" -d out src/db/*.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for db"
    exit 1
fi

echo "[4/6] Compiling threads..."
javac -cp ".:lib/sqlite-jdbc.jar:out" -d out src/threads/*.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for threads"
    exit 1
fi

echo "[5/6] Compiling gui..."
javac -cp ".:lib/sqlite-jdbc.jar:out" -d out src/gui/*.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for gui"
    exit 1
fi

echo "[6/6] Compiling Main..."
javac -cp ".:lib/sqlite-jdbc.jar:out" -d out src/Main.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for Main"
    exit 1
fi

echo ""
echo "======================================"
echo "✓ Compilation successful!"
echo "======================================"
echo ""
echo "To run the application, execute:"
echo "  ./run.sh"
echo "or"
echo "  java -cp \"out:lib/sqlite-jdbc.jar\" Main"
echo ""
