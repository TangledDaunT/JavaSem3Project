package db;

import exceptions.DatabaseConnectionException;
import model.User;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User operations
 * Implements Repository pattern with JDBC
 */
public class UserDAO implements Repository<User> {
    private Connection connection;
    
    public UserDAO() throws DatabaseConnectionException {
        this.connection = DBConnection.getInstance().getConnection();
    }
    
    @Override
    public boolean create(User user) throws DatabaseConnectionException {
        String sql = "INSERT INTO users (username, password_hash, full_name, email, is_admin, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setInt(5, user.isAdmin() ? 1 : 0);
            pstmt.setString(6, LocalDateTime.now().toString());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to create user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public User findById(String id) throws DatabaseConnectionException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to find user: " + e.getMessage(), e);
        }
    }
    
    /**
     * Find user by username
     */
    public User findByUsername(String username) throws DatabaseConnectionException {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to find user: " + e.getMessage(), e);
        }
    }
    
    /**
     * Authenticate user
     */
    public User authenticate(String username, String password) throws DatabaseConnectionException {
        User user = findByUsername(username);
        if (user != null && SecurityUtil.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }
    
    @Override
    public boolean update(User user) throws DatabaseConnectionException {
        String sql = "UPDATE users SET username = ?, full_name = ?, email = ?, is_admin = ? " +
                     "WHERE user_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getFullName());
            pstmt.setString(3, user.getEmail());
            pstmt.setInt(4, user.isAdmin() ? 1 : 0);
            pstmt.setInt(5, user.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to update user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(String id) throws DatabaseConnectionException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(id));
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to delete user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<User> findAll() throws DatabaseConnectionException {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        List<User> users = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to fetch users: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extract User object from ResultSet
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User(
            rs.getInt("user_id"),
            rs.getString("username"),
            rs.getString("password_hash"),
            rs.getString("full_name"),
            rs.getString("email"),
            rs.getInt("is_admin") == 1
        );
        user.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
        return user;
    }
}