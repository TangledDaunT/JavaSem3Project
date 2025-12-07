-- BankBridge Database Schema
-- SQLite Database for Offline Banking System

-- Enable foreign key support
PRAGMA foreign_keys = ON;

-- Users Table
-- Stores user authentication and profile information
CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    full_name TEXT NOT NULL,
    email TEXT,
    is_admin INTEGER DEFAULT 0,
    created_at TEXT NOT NULL
);

-- Accounts Table
-- Stores bank account information
CREATE TABLE IF NOT EXISTS accounts (
    account_number TEXT PRIMARY KEY,
    user_id INTEGER NOT NULL,
    account_type TEXT NOT NULL CHECK(account_type IN ('SAVINGS', 'CURRENT')),
    balance REAL NOT NULL DEFAULT 0.0,
    is_active INTEGER DEFAULT 1,
    created_at TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Transactions Table
-- Stores all banking transactions
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
    account_number TEXT NOT NULL,
    transaction_type TEXT NOT NULL CHECK(transaction_type IN ('DEPOSIT', 'WITHDRAW', 'TRANSFER')),
    amount REAL NOT NULL,
    to_account TEXT,
    description TEXT,
    timestamp TEXT NOT NULL,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE
);

-- Indexes for faster queries
CREATE INDEX IF NOT EXISTS idx_user_accounts ON accounts(user_id);
CREATE INDEX IF NOT EXISTS idx_account_transactions ON transactions(account_number);
CREATE INDEX IF NOT EXISTS idx_transaction_timestamp ON transactions(timestamp);

-- Insert default admin user (password: admin123)
INSERT OR IGNORE INTO users (username, password_hash, full_name, email, is_admin, created_at)
VALUES (
    'admin',
    '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',
    'System Administrator',
    'admin@bankbridge.com',
    1,
    datetime('now')
);

-- Insert sample test user (username: john_doe, password: password123)
INSERT OR IGNORE INTO users (username, password_hash, full_name, email, is_admin, created_at)
VALUES (
    'john_doe',
    'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f',
    'John Doe',
    'john@example.com',
    0,
    datetime('now')
);

-- Sample data: Create a savings account for john_doe
-- INSERT INTO accounts (account_number, user_id, account_type, balance, is_active, created_at)
-- VALUES ('1234567890', 2, 'SAVINGS', 5000.00, 1, datetime('now'));

-- Sample data: Create a current account for john_doe
-- INSERT INTO accounts (account_number, user_id, account_type, balance, is_active, created_at)
-- VALUES ('9876543210', 2, 'CURRENT', 10000.00, 1, datetime('now'));
