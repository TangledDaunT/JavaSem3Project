package db;

import exceptions.DatabaseConnectionException;
import java.util.List;

/**
 * Generic Repository interface for CRUD operations
 * Demonstrates Interface concept and Generic programming
 */
public interface Repository<T> {
    
    /**
     * Create a new record
     */
    boolean create(T entity) throws DatabaseConnectionException;
    
    /**
     * Read a record by ID
     */
    T findById(String id) throws DatabaseConnectionException;
    
    /**
     * Update an existing record
     */
    boolean update(T entity) throws DatabaseConnectionException;
    
    /**
     * Delete a record by ID
     */
    boolean delete(String id) throws DatabaseConnectionException;
    
    /**
     * Find all records
     */
    List<T> findAll() throws DatabaseConnectionException;
}