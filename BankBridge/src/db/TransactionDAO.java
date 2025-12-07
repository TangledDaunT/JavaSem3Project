package db;

import exceptions.DatabaseConnectionException;
import model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Transaction operations
 */
public class TransactionDAO implements Repository<Transaction> {
    private Connection connection;
    
    public TransactionDAO() throws DatabaseConnectionException {
        this.connection = DBConnection.getInstance().getConnection();
    }
    
    @Override
    public boolean create(Transaction transaction) throws DatabaseConnectionException {
        String sql = "INSERT INTO transactions (account_number, transaction_type, amount, to_account, description, timestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getAccountNumber());
            pstmt.setString(2, transaction.getTransactionType());
            pstmt.setDouble(3, transaction.getAmount());
            
            // Set to_account for transfers
            if (transaction instanceof Transfer) {
                pstmt.setString(4, ((Transfer) transaction).getToAccountNumber());
            } else {
                pstmt.setString(4, null);
            }
            
            pstmt.setString(5, transaction.getDescription());
            pstmt.setString(6, LocalDateTime.now().toString());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to create transaction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Transaction findById(String id) throws DatabaseConnectionException {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractTransactionFromResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to find transaction: " + e.getMessage(), e);
        }
    }
    
    /**
     * Find all transactions for a specific account
     */
    public List<Transaction> findByAccountNumber(String accountNumber) throws DatabaseConnectionException {
        String sql = "SELECT * FROM transactions WHERE account_number = ? OR to_account = ? " +
                     "ORDER BY timestamp DESC LIMIT 100";
        List<Transaction> transactions = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
            return transactions;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to fetch transactions: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(Transaction transaction) throws DatabaseConnectionException {
        // Transactions are typically immutable, but we can update description
        String sql = "UPDATE transactions SET description = ? WHERE transaction_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getDescription());
            pstmt.setInt(2, transaction.getTransactionId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to update transaction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(String id) throws DatabaseConnectionException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(id));
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to delete transaction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Transaction> findAll() throws DatabaseConnectionException {
        String sql = "SELECT * FROM transactions ORDER BY timestamp DESC LIMIT 1000";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
            return transactions;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to fetch transactions: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get transaction statistics
     */
    public double getTotalTransactionAmount(String accountNumber, String transactionType) 
            throws DatabaseConnectionException {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total FROM transactions " +
                     "WHERE account_number = ? AND transaction_type = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, transactionType);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0.0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to calculate transaction total: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extract Transaction object from ResultSet (Polymorphism)
     */
    private Transaction extractTransactionFromResultSet(ResultSet rs) throws SQLException {
        int transactionId = rs.getInt("transaction_id");
        String accountNumber = rs.getString("account_number");
        String transactionType = rs.getString("transaction_type");
        double amount = rs.getDouble("amount");
        String toAccount = rs.getString("to_account");
        String description = rs.getString("description");
        LocalDateTime timestamp = LocalDateTime.parse(rs.getString("timestamp"));
        
        Transaction transaction;
        switch (transactionType) {
            case "DEPOSIT":
                transaction = new Deposit(transactionId, accountNumber, amount);
                break;
            case "WITHDRAW":
                transaction = new Withdraw(transactionId, accountNumber, amount);
                break;
            case "TRANSFER":
                transaction = new Transfer(transactionId, accountNumber, toAccount, amount);
                break;
            default:
                throw new SQLException("Unknown transaction type: " + transactionType);
        }
        
        transaction.setDescription(description);
        transaction.setTimestamp(timestamp);
        return transaction;
    }
}