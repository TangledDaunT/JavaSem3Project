package exceptions;

/**
 * Exception thrown when account has insufficient balance for a transaction
 */
public class InsufficientFundsException extends Exception {
    private double currentBalance;
    private double requestedAmount;
    
    public InsufficientFundsException(double currentBalance, double requestedAmount) {
        super(String.format("Insufficient funds. Current balance: %.2f, Requested: %.2f", 
              currentBalance, requestedAmount));
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
    }
    
    public double getCurrentBalance() {
        return currentBalance;
    }
    
    public double getRequestedAmount() {
        return requestedAmount;
    }
}