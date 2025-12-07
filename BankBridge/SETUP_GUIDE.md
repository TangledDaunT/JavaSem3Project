# 🛠️ BankBridge Setup Guide

## Complete Installation Instructions for Beginners

---

## 📌 Table of Contents

1. [System Requirements](#system-requirements)
2. [Download SQLite JDBC Driver](#download-sqlite-jdbc-driver)
3. [Setup Instructions](#setup-instructions)
4. [Running the Application](#running-the-application)
5. [Troubleshooting](#troubleshooting)

---

## 💻 System Requirements

### Minimum Requirements:
- **Operating System:** Windows 7+, macOS 10.10+, or Linux
- **Java:** JDK 8 or higher
- **RAM:** 512 MB minimum, 1 GB recommended
- **Disk Space:** 50 MB for application + dependencies
- **Display:** 1024x768 minimum resolution

### Check Java Installation:

```bash
java -version
```

If Java is not installed:
- **Windows/macOS:** Download from [Oracle](https://www.oracle.com/java/technologies/downloads/)
- **Linux (Ubuntu/Debian):**
  ```bash
  sudo apt update
  sudo apt install openjdk-11-jdk
  ```

---

## 📥 Download SQLite JDBC Driver

### Option 1: Direct Download

1. Go to: https://github.com/xerial/sqlite-jdbc/releases
2. Download latest `sqlite-jdbc-X.XX.X.X.jar`
3. Place it in `BankBridge/lib/` folder

### Option 2: Using Maven

If using Maven, add to `pom.xml`:
```xml
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.46.0.0</version>
</dependency>
```

### Option 3: Using Gradle

Add to `build.gradle`:
```gradle
dependencies {
    implementation 'org.xerial:sqlite-jdbc:3.46.0.0'
}
```

---

## 🛠️ Setup Instructions

### Step 1: Create Project Structure

Ensure your directory looks like this:

```
BankBridge/
├── src/
│   ├── Main.java
│   ├── gui/
│   ├── model/
│   ├── db/
│   ├── exceptions/
│   └── threads/
├── database/
│   └── schema.sql
├── lib/
│   └── sqlite-jdbc.jar
└── README.md
```

### Step 2: Verify Files

Check that all required files exist:
```bash
cd BankBridge
find . -name "*.java" | wc -l  # Should show ~20+ files
```

---

## 🚀 Running the Application

### 🔵 Method 1: Command Line (All Platforms)

#### Linux/macOS:

```bash
# 1. Navigate to project directory
cd BankBridge

# 2. Create output directory
mkdir -p out

# 3. Compile all Java files
javac -cp ".:lib/sqlite-jdbc.jar" -d out $(find src -name "*.java")

# 4. Run the application
java -cp "out:lib/sqlite-jdbc.jar" Main
```

#### Windows (Command Prompt):

```cmd
REM 1. Navigate to project directory
cd BankBridge

REM 2. Create output directory
mkdir out

REM 3. Compile (one package at a time)
javac -cp ".;lib\sqlite-jdbc.jar" -d out src\*.java
javac -cp ".;lib\sqlite-jdbc.jar;out" -d out src\exceptions\*.java
javac -cp ".;lib\sqlite-jdbc.jar;out" -d out src\model\*.java
javac -cp ".;lib\sqlite-jdbc.jar;out" -d out src\db\*.java
javac -cp ".;lib\sqlite-jdbc.jar;out" -d out src\threads\*.java
javac -cp ".;lib\sqlite-jdbc.jar;out" -d out src\gui\*.java

REM 4. Run the application
java -cp "out;lib\sqlite-jdbc.jar" Main
```

#### Windows (PowerShell):

```powershell
# 1. Navigate to project directory
cd BankBridge

# 2. Compile
javac -cp ".;lib/sqlite-jdbc.jar" -d out (Get-ChildItem -Recurse src/*.java).FullName

# 3. Run
java -cp "out;lib/sqlite-jdbc.jar" Main
```

---

### 🟢 Method 2: IntelliJ IDEA

1. **Open Project:**
   - `File` → `Open` → Select `BankBridge` folder
   - Click `OK`

2. **Configure Project Structure:**
   - `File` → `Project Structure` (or `Ctrl+Alt+Shift+S`)
   - `Modules` → Mark `src` as **Sources Root**
   - `Libraries` → `+` → `Java` → Select `lib/sqlite-jdbc.jar`
   - Click `Apply` and `OK`

3. **Run Application:**
   - Right-click `Main.java` → `Run 'Main.main()'`
   - Or press `Shift + F10`

---

### 🟡 Method 3: Eclipse

1. **Import Project:**
   - `File` → `Import` → `General` → `Existing Projects into Workspace`
   - Select `BankBridge` folder → `Finish`

2. **Add SQLite JDBC:**
   - Right-click project → `Build Path` → `Configure Build Path`
   - `Libraries` tab → `Add External JARs`
   - Navigate to `lib/sqlite-jdbc.jar` → `Open`
   - Click `Apply and Close`

3. **Run Application:**
   - Right-click `Main.java` → `Run As` → `Java Application`

---

### 🟠 Method 4: VS Code

1. **Install Java Extension Pack**

2. **Open Project:**
   - `File` → `Open Folder` → Select `BankBridge`

3. **Configure Classpath:**
   - Create `.vscode/settings.json`:
   ```json
   {
     "java.project.sourcePaths": ["src"],
     "java.project.referencedLibraries": [
       "lib/**/*.jar"
     ]
   }
   ```

4. **Run Application:**
   - Open `Main.java`
   - Click `Run` or press `F5`

---

## 🛡️ Troubleshooting

### Problem 1: "ClassNotFoundException: org.sqlite.JDBC"

**Solution:**
- Verify `sqlite-jdbc.jar` is in `lib/` folder
- Check classpath includes the JAR:
  ```bash
  java -cp "out:lib/sqlite-jdbc.jar" Main  # Linux/macOS
  java -cp "out;lib/sqlite-jdbc.jar" Main  # Windows
  ```

---

### Problem 2: "Error: Could not find or load main class Main"

**Solution:**
- Ensure you're in the project root directory
- Verify compilation created `out/Main.class`
- Check that `out/` is included in classpath

---

### Problem 3: "Database connection failed"

**Solution:**
- Create `database/` directory if it doesn't exist:
  ```bash
  mkdir database
  ```
- Check write permissions:
  ```bash
  chmod 755 database
  ```
- Verify path in `DBConnection.java` points to `jdbc:sqlite:database/bank.db`

---

### Problem 4: GUI doesn't appear

**Solution:**
- Check if Java AWT/Swing is supported:
  ```bash
  java -version  # Should show desktop version, not headless
  ```
- On Linux, install:
  ```bash
  sudo apt install openjfx
  ```

---

### Problem 5: "package X does not exist"

**Solution:**
- Compile packages in order:
  1. exceptions
  2. model
  3. db
  4. threads
  5. gui
  6. Main

---

### Problem 6: Permission denied on Linux/macOS

**Solution:**
```bash
chmod +x run.sh  # If using a shell script
chmod 755 database/
```

---

## ✅ Verification Checklist

Before running, verify:

- [ ] Java JDK installed (`java -version` works)
- [ ] `sqlite-jdbc.jar` in `lib/` folder
- [ ] All source files in `src/` with proper package structure
- [ ] `database/` directory exists
- [ ] Compilation completes without errors
- [ ] Classpath includes both `out/` and `lib/sqlite-jdbc.jar`

---

## 📞 Need Help?

If you're still facing issues:

1. **Check Java installation:**
   ```bash
   java -version
   javac -version
   ```

2. **Verify JDBC driver:**
   ```bash
   jar tf lib/sqlite-jdbc.jar | grep "org.sqlite.JDBC"
   ```

3. **Test database access:**
   ```bash
   sqlite3 database/bank.db ".tables"
   ```

---

## 🎉 Success!

If everything is set up correctly, you should see:

```
============================================================
       🏦 BANKBRIDGE - Banking Management System
============================================================

[1/4] Initializing database connection...
✓ Database connection successful

[2/4] Checking for default admin user...
✓ Admin user check complete

[3/4] Creating sample test users...
✓ Sample users created

[4/4] Launching user interface...
✓ GUI launched successfully

============================================================

✅ BankBridge is now running!

Default Admin Credentials:
  Username: admin
  Password: admin123
```

The login window should appear, and you're ready to use BankBridge!

---

*Happy Banking! 🏦*