package db;

import exceptions.DatabaseConnectionException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton class for database connection management
 * Handles SQLite database connection and initialization
 */
public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:database/bank.db";
    
    private DBConnection() throws DatabaseConnectionException {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Establish connection
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connection established successfully.");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            throw new DatabaseConnectionException("SQLite JDBC driver not found", e);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to database", e);
        }
    }
    
    /**
     * Get singleton instance of database connection
     */
    public static synchronized DBConnection getInstance() throws DatabaseConnectionException {
        if (instance == null || !isConnectionValid()) {
            instance = new DBConnection();
        }
        return instance;
    }
    
    /**
     * Get the active connection
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Check if connection is valid
     */
    private static boolean isConnectionValid() {
        try {
            return instance != null && 
                   instance.connection != null && 
                   !instance.connection.isClosed() &&
                   instance.connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Initialize database schema if not exists
     */
    private void initializeDatabase() throws DatabaseConnectionException {
        try (Statement stmt = connection.createStatement()) {
            // Enable foreign keys
            stmt.execute("PRAGMA foreign_keys = ON");
            
            // Create users table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password_hash TEXT NOT NULL," +
                "full_name TEXT NOT NULL," +
                "email TEXT," +
                "is_admin INTEGER DEFAULT 0," +
                "created_at TEXT NOT NULL" +
                ")"
            );
            
            // Create accounts table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_number TEXT PRIMARY KEY," +
                "user_id INTEGER NOT NULL," +
                "account_type TEXT NOT NULL CHECK(account_type IN ('SAVINGS', 'CURRENT'))," +
                "balance REAL NOT NULL DEFAULT 0.0," +
                "is_active INTEGER DEFAULT 1," +
                "created_at TEXT NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE" +
                ")"
            );
            
            // Create transactions table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS transactions (" +
                "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "account_number TEXT NOT NULL," +
                "transaction_type TEXT NOT NULL CHECK(transaction_type IN ('DEPOSIT', 'WITHDRAW', 'TRANSFER'))," +
                "amount REAL NOT NULL," +
                "to_account TEXT," +
                "description TEXT," +
                "timestamp TEXT NOT NULL," +
                "FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE" +
                ")"
            );
            
            // Create index for faster queries
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_user_accounts ON accounts(user_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_account_transactions ON transactions(account_number)");
            
            System.out.println("Database schema initialized successfully.");
            
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to initialize database schema", e);
        }
    }
    
    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}