# 🏦 BankBridge - Project Information

## 📊 Project Statistics

- **Total Java Files:** 28
- **Total Lines of Code:** ~3,500+
- **Packages:** 5 (gui, model, db, exceptions, threads)
- **Classes:** 26
- **Interfaces:** 2

## 📁 File Structure Overview

```
BankBridge/
├── src/                              [28 Java files, ~3,500 LOC]
│   ├── Main.java                     [Entry point, initialization]
│   ├── gui/                          [7 GUI classes]
│   │   ├── LoginFrame.java           [Login screen with authentication]
│   │   ├── RegisterDialog.java       [User registration form]
│   │   ├── Dashboard.java            [Main dashboard after login]
│   │   ├── CreateAccountForm.java    [Account creation interface]
│   │   ├── TransactionForm.java      [Deposit/Withdraw/Transfer UI]
│   │   ├── TransactionHistoryDialog.java [Transaction logs viewer]
│   │   └── AdminPanel.java           [Admin control panel]
│   ├── model/                        [10 domain model classes]
│   │   ├── BankEntity.java           [Base interface for entities]
│   │   ├── Account.java              [Abstract account class]
│   │   ├── SavingsAccount.java       [Savings account with interest]
│   │   ├── CurrentAccount.java       [Current account with overdraft]
│   │   ├── Transaction.java          [Abstract transaction class]
│   │   ├── Deposit.java              [Deposit transaction]
│   │   ├── Withdraw.java             [Withdrawal transaction]
│   │   ├── Transfer.java             [Transfer transaction]
│   │   └── User.java                 [User entity]
│   ├── db/                           [5 database classes]
│   │   ├── DBConnection.java         [Singleton connection manager]
│   │   ├── Repository.java           [Generic DAO interface]
│   │   ├── SecurityUtil.java         [Password hashing utilities]
│   │   ├── UserDAO.java              [User data access object]
│   │   ├── AccountDAO.java           [Account data access object]
│   │   └── TransactionDAO.java       [Transaction data access object]
│   ├── exceptions/                   [3 custom exception classes]
│   │   ├── InsufficientFundsException.java
│   │   ├── InvalidAccountException.java
│   │   └── DatabaseConnectionException.java
│   └── threads/                      [2 multithreading classes]
│       ├── TransactionEngine.java    [Async transaction processor]
│       └── AuditThread.java          [Background audit thread]
├── database/
│   ├── schema.sql                    [Database schema & init data]
│   └── bank.db                       [SQLite database (auto-created)]
├── lib/
│   └── DOWNLOAD_JDBC.txt             [Instructions for JDBC driver]
├── compile.sh / compile.bat          [Compilation scripts]
├── run.sh / run.bat                  [Run scripts]
├── README.md                         [Main documentation]
├── SETUP_GUIDE.md                    [Detailed setup instructions]
└── PROJECT_INFO.md                   [This file]
```

## 🎯 OOP Concepts Implemented

### 1. Abstraction
- **Abstract Classes:**
  - `Account.java` - Base class for all account types
  - `Transaction.java` - Base class for all transactions
- **Abstract Methods:**
  - `applyInterest()`, `getMinimumBalance()`, `getAccountFeatures()`
  - `execute(Account account)`

### 2. Inheritance
- **Class Hierarchy:**
  ```
  Account (abstract)
      ├── SavingsAccount
      └── CurrentAccount
  
  Transaction (abstract)
      ├── Deposit
      ├── Withdraw
      └── Transfer
  ```

### 3. Polymorphism
- **Method Overriding:**
  - `applyInterest()` behaves differently in SavingsAccount vs CurrentAccount
  - `execute()` implemented differently for each transaction type
- **Runtime Polymorphism:**
  - Account objects can be SavingsAccount or CurrentAccount
  - Transaction processing works for any transaction type

### 4. Encapsulation
- **All model classes have:**
  - Private fields
  - Public getters/setters
  - Input validation
  - Data hiding

### 5. Interfaces
- `BankEntity` - Common behavior for all entities
- `Repository<T>` - Generic CRUD operations
- `TransactionCallback` - Callback pattern for async operations

### 6. Method Overloading
- Multiple constructors in Account classes
- Different parameter combinations for object creation

### 7. Constructor Chaining
- `this()` calls in constructors
- Parent constructor calls using `super()`

## 🗄️ Database Design

### Tables:

#### users
```sql
user_id (PK)
username (UNIQUE)
password_hash
full_name
email
is_admin
created_at
```

#### accounts
```sql
account_number (PK)
user_id (FK → users)
account_type
balance
is_active
created_at
```

#### transactions
```sql
transaction_id (PK)
account_number (FK → accounts)
transaction_type
amount
to_account
description
timestamp
```

### Indexes:
- `idx_user_accounts` - On accounts(user_id)
- `idx_account_transactions` - On transactions(account_number)
- `idx_transaction_timestamp` - On transactions(timestamp)

## 🧵 Multithreading Implementation

### 1. TransactionEngine (Runnable)
- **Purpose:** Asynchronous transaction processing
- **Features:**
  - BlockingQueue for thread-safe queuing
  - Non-blocking transaction submission
  - Callback mechanism for results
  - Graceful shutdown support
- **Thread Safety:** Synchronized account balance updates

### 2. AuditThread (extends Thread)
- **Purpose:** Background monitoring and logging
- **Features:**
  - Periodic system audits (every 60 seconds)
  - Total balance calculation
  - Suspicious transaction detection
  - Daemon thread (doesn't block JVM shutdown)
- **Monitoring:**
  - Total accounts and users
  - System-wide balance
  - Recent transactions
  - Large transaction alerts

## 🔒 Security Features

### 1. Password Security
- **Algorithm:** SHA-256 hashing
- **Implementation:** `SecurityUtil.hashPassword()`
- **Verification:** Constant-time comparison

### 2. SQL Injection Prevention
- **Method:** PreparedStatements only
- **No String Concatenation:** All queries parameterized

### 3. Transaction Safety
- **ACID Compliance:**
  - Atomic: All or nothing
  - Consistent: Database constraints enforced
  - Isolated: Proper locking
  - Durable: Commits to disk

### 4. Thread Safety
- **Synchronized Methods:** Balance updates
- **Atomic Operations:** Single transaction per thread
- **BlockingQueue:** Thread-safe queuing

### 5. Input Validation
- Username length (min 3 characters)
- Password length (min 6 characters)
- Amount validation (positive numbers)
- Account type validation (enum checks)

## 📊 Class Relationships

```
Main
 └──> LoginFrame
       ├──> RegisterDialog → UserDAO
       └──> Dashboard
             ├──> CreateAccountForm → AccountDAO
             ├──> TransactionForm → TransactionEngine → AccountDAO
             ├──> TransactionHistoryDialog → TransactionDAO
             └──> AdminPanel → UserDAO, AccountDAO, TransactionDAO

DBConnection (Singleton)
 └──> Used by all DAO classes

TransactionEngine
 └──> Uses AccountDAO and TransactionDAO

AuditThread
 └──> Uses AccountDAO and TransactionDAO
```

## 🎨 GUI Components

### Windows:
1. **LoginFrame** - Main entry point
2. **Dashboard** - Central hub
3. **AdminPanel** - Admin-only window

### Dialogs:
1. **RegisterDialog** - User registration
2. **CreateAccountForm** - Account creation
3. **TransactionForm** - Transaction execution
4. **TransactionHistoryDialog** - Transaction logs

### UI Features:
- Custom color schemes
- Gradient backgrounds
- Rounded borders
- Table views with sorting
- Real-time balance updates
- Responsive layouts

## 🔄 Application Flow

```
1. Main.java
   ↓
2. Initialize Database (DBConnection)
   ↓
3. Create default admin user
   ↓
4. Launch LoginFrame
   ↓
5. User Login/Register
   ↓
6. Start Background Threads
   ├── TransactionEngine (async processing)
   └── AuditThread (monitoring)
   ↓
7. Open Dashboard
   ↓
8. User Actions:
   ├── Create Account
   ├── Deposit/Withdraw/Transfer
   ├── View History
   └── Admin Panel (if admin)
   ↓
9. Logout
   ↓
10. Stop Background Threads
    ↓
11. Return to Login
```

## 📦 Collections Used

1. **ArrayList<>** - For storing lists of users, accounts, transactions
2. **BlockingQueue<>** - Thread-safe transaction queuing
3. **List<>** - Generic list interface usage

## 🚀 Design Patterns

1. **Singleton** - DBConnection
2. **DAO (Data Access Object)** - UserDAO, AccountDAO, TransactionDAO
3. **Repository** - Generic CRUD interface
4. **Factory** - Account creation (Savings/Current)
5. **Callback** - TransactionCallback interface
6. **MVC** - Model-View-Controller separation
7. **Template Method** - Abstract classes with template methods

## 📈 Performance Considerations

- **Database Connection:** Singleton pattern prevents multiple connections
- **Transaction Processing:** Asynchronous via TransactionEngine
- **GUI Updates:** SwingUtilities.invokeLater() for thread safety
- **Database Queries:** Prepared statements with parameter binding
- **Indexing:** Database indexes on frequently queried columns

## 🧪 Testing Approach

### Manual Testing:
1. User registration and login
2. Account creation (both types)
3. Deposit operations
4. Withdrawal operations
5. Transfer between accounts
6. Transaction history viewing
7. Admin panel functionality
8. Concurrent transactions

### Test Data:
- Default admin: admin/admin123
- Sample user: john_doe/password123

## 📚 Learning Outcomes

This project demonstrates:
- ✅ Complete OOP implementation
- ✅ JDBC database operations
- ✅ DAO pattern for data access
- ✅ Multithreading concepts
- ✅ GUI development with Swing
- ✅ Exception handling
- ✅ Security best practices
- ✅ Software architecture design
- ✅ Design patterns
- ✅ Professional code organization

## 🎓 Academic Evaluation Points

### Core Java Concepts:
- ✅ Classes and Objects
- ✅ Inheritance
- ✅ Polymorphism
- ✅ Abstraction
- ✅ Encapsulation
- ✅ Interfaces

### Advanced Concepts:
- ✅ Exception Handling
- ✅ Collections Framework
- ✅ Multithreading
- ✅ File I/O (database)
- ✅ JDBC

### Software Engineering:
- ✅ Design Patterns
- ✅ Layered Architecture
- ✅ Code Organization
- ✅ Documentation

## 🎯 Project Complexity: HIGH

- **Lines of Code:** 3,500+
- **Classes:** 26
- **Packages:** 5
- **Database Tables:** 3
- **GUI Screens:** 7
- **Threads:** 2
- **Design Patterns:** 7+

---

**Built with ❤️ for academic excellence**

*Shreyansh Misra & Shivam*
*B.Tech CSE, Galgotias University*
