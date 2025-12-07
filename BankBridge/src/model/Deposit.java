package model;

/**
 * Deposit transaction implementation
 * Demonstrates Method Overriding
 */
public class Deposit extends Transaction {
    
    public Deposit(int transactionId, String accountNumber, double amount) {
        super(transactionId, accountNumber, amount, "DEPOSIT");
        this.description = "Deposit to account";
    }
    
    public Deposit(String accountNumber, double amount) {
        super(accountNumber, amount, "DEPOSIT");
        this.description = "Deposit to account";
    }
    
    @Override
    public boolean execute(Account account) throws Exception {
        validate();
        if (!account.getAccountNumber().equals(this.accountNumber)) {
            throw new IllegalArgumentException("Account number mismatch");
        }
        
        account.deposit(this.amount);
        System.out.println("Deposit successful: $" + String.format("%.2f", amount));
        return true;
    }
}