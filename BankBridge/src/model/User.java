package model;

import java.time.LocalDateTime;

/**
 * User entity for authentication
 * Demonstrates Encapsulation
 */
public class User implements BankEntity {
    private int userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private boolean isAdmin;
    private LocalDateTime createdAt;
    
    public User(int userId, String username, String passwordHash, String fullName, String email, boolean isAdmin) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.createdAt = LocalDateTime.now();
    }
    
    public User(String username, String passwordHash, String fullName, String email) {
        this(0, username, passwordHash, fullName, email, false);
    }
    
    @Override
    public String getId() {
        return String.valueOf(userId);
    }
    
    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public void validate() throws IllegalArgumentException {
        if (username == null || username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters");
        }
        if (passwordHash == null || passwordHash.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("%s (%s) - %s", fullName, username, isAdmin ? "Admin" : "User");
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public boolean isAdmin() {
        return isAdmin;
    }
    
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}