package exceptions;

/**
 * Exception thrown when database connection or operation fails
 */
public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException(String message) {
        super("Database Error: " + message);
    }
    
    public DatabaseConnectionException(String message, Throwable cause) {
        super("Database Error: " + message, cause);
    }
}