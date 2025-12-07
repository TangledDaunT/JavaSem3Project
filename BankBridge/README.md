# 🏦 BankBridge - Offline Banking Management System

A comprehensive Java Swing + SQLite banking application demonstrating **OOP, JDBC, Collections, Multithreading & DAO Architecture**.

---

## 📚 Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Architecture](#architecture)
4. [OOP Concepts Implemented](#oop-concepts-implemented)
5. [Installation & Setup](#installation--setup)
6. [How to Run](#how-to-run)
7. [Usage Guide](#usage-guide)
8. [Database Schema](#database-schema)
9. [Technologies Used](#technologies-used)
10. [Project Structure](#project-structure)
11. [Security Features](#security-features)
12. [Future Enhancements](#future-enhancements)
13. [Authors](#authors)

---

## 📝 Overview

**BankBridge** is a fully functional offline banking management system built with Java that showcases enterprise-level software design patterns. It provides a complete banking solution with user authentication, account management, transaction processing, and administrative controls.

### Key Highlights:
- ✅ **Complete OOP Implementation** - Abstraction, Inheritance, Polymorphism, Encapsulation, Interfaces
- ✅ **JDBC Database Integration** - SQLite with PreparedStatements
- ✅ **DAO Pattern** - Clean separation of data access logic
- ✅ **Multithreading** - Asynchronous transaction processing and background auditing
- ✅ **Modern Swing GUI** - Professional user interface with custom styling
- ✅ **Secure** - SHA-256 password hashing, SQL injection prevention
- ✅ **Transaction Management** - ACID properties with commit/rollback

---

## ✨ Features

### User Features:
- 🔐 **Secure Login & Registration** - SHA-256 encrypted passwords
- 🏦 **Account Creation** - Savings and Current accounts
- 💵 **Deposit & Withdrawal** - Real-time balance updates
- 🔄 **Money Transfer** - Between accounts with transaction safety
- 📊 **Transaction History** - Complete audit trail
- 👤 **User Dashboard** - View all accounts and balances

### Admin Features:
- 🔑 **Admin Panel** - Complete system overview
- 👥 **User Management** - View all registered users
- 🏦 **Account Monitoring** - View all accounts across system
- 📊 **System Statistics** - Total balances, transaction counts
- 🔍 **Audit Logs** - Background monitoring thread

### Technical Features:
- ⚡ **Asynchronous Processing** - Non-blocking transaction execution
- 🔄 **Auto-refresh** - Real-time data updates
- 🛡️ **Exception Handling** - Custom exceptions for banking operations
- 💾 **Offline Capable** - No internet required
- 🧵 **Thread-safe** - Synchronized balance operations

---

## 🏛️ Architecture

### Layered Architecture:

```
┌────────────────────────┐
│   Presentation Layer    │  ← Java Swing GUI
│   (gui package)         │
└─────────┬──────────────┘
          │
┌─────────┴──────────────┐
│   Business Logic Layer  │  ← Domain Models
│   (model package)       │
└─────────┬──────────────┘
          │
┌─────────┴──────────────┐
│   Data Access Layer     │  ← DAO + JDBC
│   (db package)          │
└─────────┬──────────────┘
          │
┌─────────┴──────────────┐
│   Database Layer        │  ← SQLite
│   (database/bank.db)    │
└────────────────────────┘
```

### Design Patterns Used:
- **Singleton Pattern** - DBConnection
- **DAO Pattern** - UserDAO, AccountDAO, TransactionDAO
- **Repository Pattern** - Generic Repository interface
- **Factory Pattern** - Account creation (Savings/Current)
- **MVC Pattern** - Separation of GUI, Model, and Data layers

---

## 🏛️ OOP Concepts Implemented

| Concept | Implementation | Location |
|---------|----------------|----------|
| **Abstraction** | Abstract classes `Account` and `Transaction` | model/Account.java |
| **Inheritance** | `SavingsAccount` and `CurrentAccount` extend `Account` | model/SavingsAccount.java |
| **Polymorphism** | Different behavior for `applyInterest()` in account types | model/Account.java |
| **Encapsulation** | Private fields with getters/setters | All model classes |
| **Interfaces** | `BankEntity` and `Repository<T>` interfaces | model/BankEntity.java |
| **Method Overriding** | Transaction types override `execute()` | model/Deposit.java |
| **Method Overloading** | Multiple constructors in Account classes | model/Account.java |
| **Constructor Chaining** | `this()` calls in constructors | model/Account.java |

---

## 🛠️ Installation & Setup

### Prerequisites:

1. **Java Development Kit (JDK) 8 or higher**
   ```bash
   java -version
   ```

2. **SQLite JDBC Driver**
   - Download from: https://github.com/xerial/sqlite-jdbc
   - Or Maven: `org.xerial:sqlite-jdbc:3.46.0.0`
   - Place `sqlite-jdbc.jar` in the `lib/` folder

### Directory Structure:

```
BankBridge/
├── src/                  # Java source files
│   ├── Main.java
│   ├── gui/              # GUI components
│   ├── model/            # Domain models
│   ├── db/               # Database layer
│   ├── exceptions/       # Custom exceptions
│   └── threads/          # Multithreading
├── database/             # SQLite database
│   ├── bank.db           # Database file (auto-created)
│   └── schema.sql        # Database schema
├── lib/                  # External libraries
│   └── sqlite-jdbc.jar   # JDBC driver
└── README.md             # This file
```

---

## 🚀 How to Run

### Method 1: Using Terminal (Recommended)

1. **Navigate to project directory:**
   ```bash
   cd BankBridge
   ```

2. **Compile all Java files:**
   ```bash
   javac -cp ".:lib/sqlite-jdbc.jar" -d out $(find src -name "*.java")
   ```
   
   *On Windows:*
   ```cmd
   javac -cp ".;lib/sqlite-jdbc.jar" -d out src/**/*.java
   ```

3. **Run the application:**
   ```bash
   java -cp "out:lib/sqlite-jdbc.jar" Main
   ```
   
   *On Windows:*
   ```cmd
   java -cp "out;lib/sqlite-jdbc.jar" Main
   ```

### Method 2: Using IDE (IntelliJ IDEA / Eclipse)

#### IntelliJ IDEA:
1. Open IntelliJ → `Open Project` → Select `BankBridge` folder
2. Mark `src/` as **Sources Root**
3. Add `sqlite-jdbc.jar` to project libraries:
   - `File` → `Project Structure` → `Modules` → `Dependencies` → `+` → `JARs`
4. Run `Main.java`

#### Eclipse:
1. `File` → `Import` → `Existing Projects into Workspace`
2. Right-click project → `Build Path` → `Configure Build Path`
3. `Libraries` tab → `Add External JARs` → Select `sqlite-jdbc.jar`
4. Run `Main.java`

---

## 📋 Usage Guide

### 1. First Launch

When you run the application for the first time:
- Database is automatically created
- Default admin user is created
- Sample test users are created

### 2. Login Credentials

**Admin Account:**
- Username: `admin`
- Password: `admin123`

**Sample User:**
- Username: `john_doe`
- Password: `password123`

### 3. User Workflow

1. **Register/Login** → Enter credentials
2. **Create Account** → Choose Savings or Current
3. **Perform Transactions** → Deposit/Withdraw/Transfer
4. **View History** → Check transaction logs
5. **Logout** → Secure exit

### 4. Admin Workflow

1. **Login as Admin** → Use admin credentials
2. **Open Admin Panel** → Click "Admin Panel" button
3. **View Statistics** → Monitor system health
4. **Manage Users** → View all users and accounts

---

## 💾 Database Schema

### Tables:

#### 1. **users**
```sql
CREATE TABLE users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    full_name TEXT NOT NULL,
    email TEXT,
    is_admin INTEGER DEFAULT 0,
    created_at TEXT NOT NULL
);
```

#### 2. **accounts**
```sql
CREATE TABLE accounts (
    account_number TEXT PRIMARY KEY,
    user_id INTEGER NOT NULL,
    account_type TEXT CHECK(account_type IN ('SAVINGS', 'CURRENT')),
    balance REAL NOT NULL DEFAULT 0.0,
    is_active INTEGER DEFAULT 1,
    created_at TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

#### 3. **transactions**
```sql
CREATE TABLE transactions (
    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
    account_number TEXT NOT NULL,
    transaction_type TEXT CHECK(transaction_type IN ('DEPOSIT', 'WITHDRAW', 'TRANSFER')),
    amount REAL NOT NULL,
    to_account TEXT,
    description TEXT,
    timestamp TEXT NOT NULL,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);
```

---

## 💻 Technologies Used

- **Java 8+** - Core programming language
- **Java Swing** - GUI framework
- **SQLite** - Embedded database
- **JDBC** - Database connectivity
- **Multithreading** - Concurrent processing
- **SHA-256** - Password encryption

---

## 📚 Project Structure

```
src/
├── Main.java                      # Entry point
├── gui/                           # User Interface
│   ├── LoginFrame.java            # Login screen
│   ├── RegisterDialog.java        # Registration
│   ├── Dashboard.java             # Main dashboard
│   ├── CreateAccountForm.java     # Account creation
│   ├── TransactionForm.java       # Transaction UI
│   ├── TransactionHistoryDialog.java
│   └── AdminPanel.java            # Admin interface
├── model/                         # Domain Models
│   ├── BankEntity.java            # Interface
│   ├── Account.java               # Abstract class
│   ├── SavingsAccount.java
│   ├── CurrentAccount.java
│   ├── Transaction.java           # Abstract class
│   ├── Deposit.java
│   ├── Withdraw.java
│   ├── Transfer.java
│   └── User.java
├── db/                            # Data Access Layer
│   ├── DBConnection.java          # Singleton connection
│   ├── Repository.java            # Generic interface
│   ├── UserDAO.java
│   ├── AccountDAO.java
│   ├── TransactionDAO.java
│   └── SecurityUtil.java          # Hashing utilities
├── exceptions/                    # Custom Exceptions
│   ├── InsufficientFundsException.java
│   ├── InvalidAccountException.java
│   └── DatabaseConnectionException.java
└── threads/                       # Multithreading
    ├── TransactionEngine.java     # Async processing
    └── AuditThread.java           # Background monitoring
```

---

## 🔒 Security Features

1. **Password Hashing** - SHA-256 encryption
2. **SQL Injection Prevention** - PreparedStatements
3. **Transaction Safety** - ACID compliance with rollback
4. **Thread Safety** - Synchronized balance operations
5. **Input Validation** - All user inputs validated
6. **Session Management** - Secure logout functionality

---

## 🚀 Future Enhancements

- [ ] Email notifications for transactions
- [ ] PDF bank statements
- [ ] Loan management system
- [ ] Fixed deposit accounts
- [ ] Multi-currency support
- [ ] Spring Boot REST API migration
- [ ] Android/iOS mobile app
- [ ] Biometric authentication
- [ ] Real-time analytics dashboard

---

## 👥 Authors

**Shreyansh Misra & Shivam**
- B.Tech CSE
- Galgotias University

---

## 📜 License

This project is open-source and available for academic and educational purposes.

---

## 📧 Support

For issues or questions:
- Create an issue on GitHub
- Contact: admin@bankbridge.com

---

**⭐ If you found this project helpful, please give it a star!**

---

*Built with ❤️ using Java*