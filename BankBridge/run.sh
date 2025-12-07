#!/bin/bash

# BankBridge Run Script
# This script runs the compiled application

echo "======================================"
echo "  Starting BankBridge Application"
echo "======================================"
echo ""

# Check if compiled
if [ ! -d "out" ] || [ ! -f "out/Main.class" ]; then
    echo "Application not compiled. Running compilation first..."
    echo ""
    ./compile.sh
    if [ $? -ne 0 ]; then
        echo "❌ Compilation failed. Cannot run application."
        exit 1
    fi
fi

# Create database directory if not exists
mkdir -p database

# Run the application
java -cp "out:lib/sqlite-jdbc.jar" Main
