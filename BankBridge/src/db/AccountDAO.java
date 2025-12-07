package db;

import exceptions.DatabaseConnectionException;
import exceptions.InvalidAccountException;
import model.Account;
import model.SavingsAccount;
import model.CurrentAccount;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Account operations
 * Demonstrates JDBC operations with PreparedStatement
 */
public class AccountDAO implements Repository<Account> {
    private Connection connection;
    
    public AccountDAO() throws DatabaseConnectionException {
        this.connection = DBConnection.getInstance().getConnection();
    }
    
    @Override
    public boolean create(Account account) throws DatabaseConnectionException {
        String sql = "INSERT INTO accounts (account_number, user_id, account_type, balance, is_active, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setInt(2, account.getUserId());
            pstmt.setString(3, account.getAccountType());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setInt(5, account.isActive() ? 1 : 0);
            pstmt.setString(6, LocalDateTime.now().toString());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to create account: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Account findById(String accountNumber) throws DatabaseConnectionException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractAccountFromResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to find account: " + e.getMessage(), e);
        }
    }
    
    /**
     * Find all accounts for a specific user
     */
    public List<Account> findByUserId(int userId) throws DatabaseConnectionException {
        String sql = "SELECT * FROM accounts WHERE user_id = ? ORDER BY created_at DESC";
        List<Account> accounts = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                accounts.add(extractAccountFromResultSet(rs));
            }
            return accounts;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to fetch user accounts: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(Account account) throws DatabaseConnectionException {
        String sql = "UPDATE accounts SET balance = ?, is_active = ? WHERE account_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, account.getBalance());
            pstmt.setInt(2, account.isActive() ? 1 : 0);
            pstmt.setString(3, account.getAccountNumber());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to update account: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update account balance with transaction support
     */
    public boolean updateBalance(String accountNumber, double newBalance) throws DatabaseConnectionException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to update balance: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(String accountNumber) throws DatabaseConnectionException {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to delete account: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Account> findAll() throws DatabaseConnectionException {
        String sql = "SELECT * FROM accounts ORDER BY created_at DESC";
        List<Account> accounts = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                accounts.add(extractAccountFromResultSet(rs));
            }
            return accounts;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to fetch accounts: " + e.getMessage(), e);
        }
    }
    
    /**
     * Transfer amount between two accounts (with transaction management)
     */
    public boolean transfer(String fromAccount, String toAccount, double amount) 
            throws DatabaseConnectionException, InvalidAccountException {
        
        try {
            // Disable auto-commit for transaction
            connection.setAutoCommit(false);
            
            // Get both accounts
            Account source = findById(fromAccount);
            Account destination = findById(toAccount);
            
            if (source == null) {
                throw new InvalidAccountException(fromAccount, "Source account not found");
            }
            if (destination == null) {
                throw new InvalidAccountException(toAccount, "Destination account not found");
            }
            
            // Perform transfer
            source.withdraw(amount);
            destination.deposit(amount);
            
            // Update both accounts in database
            updateBalance(fromAccount, source.getBalance());
            updateBalance(toAccount, destination.getBalance());
            
            // Commit transaction
            connection.commit();
            connection.setAutoCommit(true);
            
            System.out.println("Transfer completed successfully");
            return true;
            
        } catch (Exception e) {
            // Rollback on any error
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                throw new DatabaseConnectionException("Failed to rollback transaction", rollbackEx);
            }
            throw new DatabaseConnectionException("Transfer failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extract Account object from ResultSet (Polymorphism)
     */
    private Account extractAccountFromResultSet(ResultSet rs) throws SQLException {
        String accountNumber = rs.getString("account_number");
        int userId = rs.getInt("user_id");
        String accountType = rs.getString("account_type");
        double balance = rs.getDouble("balance");
        boolean isActive = rs.getInt("is_active") == 1;
        LocalDateTime createdAt = LocalDateTime.parse(rs.getString("created_at"));
        
        Account account;
        if ("SAVINGS".equals(accountType)) {
            account = new SavingsAccount(accountNumber, userId, balance);
        } else {
            account = new CurrentAccount(accountNumber, userId, balance);
        }
        
        account.setActive(isActive);
        account.setCreatedAt(createdAt);
        return account;
    }
}