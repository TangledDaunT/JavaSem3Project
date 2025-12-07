package model;

import exceptions.InsufficientFundsException;
import java.time.LocalDateTime;

/**
 * Abstract base class for all account types
 * Demonstrates Abstraction and Inheritance in OOP
 */
public abstract class Account implements BankEntity {
    protected String accountNumber;
    protected int userId;
    protected double balance;
    protected String accountType;
    protected LocalDateTime createdAt;
    protected boolean isActive;
    
    // Constructor with all parameters
    public Account(String accountNumber, int userId, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
        this.accountType = accountType;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
    
    // Constructor overloading - with initial balance
    public Account(String accountNumber, int userId, String accountType, double initialBalance) {
        this(accountNumber, userId, initialBalance, accountType);
    }
    
    // Constructor overloading - without initial balance
    public Account(String accountNumber, int userId, String accountType) {
        this(accountNumber, userId, 0.0, accountType);
    }
    
    // Abstract methods - must be implemented by subclasses
    public abstract void applyInterest();
    public abstract double getMinimumBalance();
    public abstract String getAccountFeatures();
    
    // Synchronized method for thread-safe balance updates
    public synchronized void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance += amount;
    }
    
    // Synchronized method for thread-safe withdrawals
    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (this.balance - amount < getMinimumBalance()) {
            throw new InsufficientFundsException(this.balance, amount);
        }
        this.balance -= amount;
    }
    
    @Override
    public void validate() throws IllegalArgumentException {
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be empty");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (balance < getMinimumBalance()) {
            throw new IllegalArgumentException("Balance below minimum required");
        }
    }
    
    @Override
    public String getId() {
        return accountNumber;
    }
    
    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("%s - %s (Balance: $%.2f)", accountNumber, accountType, balance);
    }
    
    // Getters and Setters (Encapsulation)
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}