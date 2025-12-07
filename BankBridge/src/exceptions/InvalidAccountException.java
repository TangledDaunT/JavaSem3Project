package exceptions;

/**
 * Exception thrown when account operation is invalid or account not found
 */
public class InvalidAccountException extends Exception {
    private String accountNumber;
    
    public InvalidAccountException(String accountNumber) {
        super("Invalid or non-existent account: " + accountNumber);
        this.accountNumber = accountNumber;
    }
    
    public InvalidAccountException(String accountNumber, String message) {
        super(message);
        this.accountNumber = accountNumber;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
}