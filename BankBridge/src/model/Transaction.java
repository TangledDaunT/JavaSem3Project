package model;

import java.time.LocalDateTime;

/**
 * Abstract base class for all transaction types
 * Demonstrates Abstraction in OOP
 */
public abstract class Transaction implements BankEntity {
    protected int transactionId;
    protected String accountNumber;
    protected double amount;
    protected String transactionType;
    protected LocalDateTime timestamp;
    protected String description;
    
    public Transaction(int transactionId, String accountNumber, double amount, String transactionType) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor overloading
    public Transaction(String accountNumber, double amount, String transactionType) {
        this(0, accountNumber, amount, transactionType);
    }
    
    // Abstract method to be implemented by subclasses
    public abstract boolean execute(Account account) throws Exception;
    
    @Override
    public void validate() throws IllegalArgumentException {
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
    }
    
    @Override
    public String getId() {
        return String.valueOf(transactionId);
    }
    
    @Override
    public LocalDateTime getCreatedAt() {
        return timestamp;
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("%s - $%.2f on %s", transactionType, amount, 
                           timestamp.toString().substring(0, 19));
    }
    
    // Getters
    public int getTransactionId() {
        return transactionId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getTransactionType() {
        return transactionType;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}