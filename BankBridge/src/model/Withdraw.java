package model;

import exceptions.InsufficientFundsException;

/**
 * Withdrawal transaction implementation
 * Demonstrates Method Overriding and Exception Handling
 */
public class Withdraw extends Transaction {
    
    public Withdraw(int transactionId, String accountNumber, double amount) {
        super(transactionId, accountNumber, amount, "WITHDRAW");
        this.description = "Withdrawal from account";
    }
    
    public Withdraw(String accountNumber, double amount) {
        super(accountNumber, amount, "WITHDRAW");
        this.description = "Withdrawal from account";
    }
    
    @Override
    public boolean execute(Account account) throws Exception {
        validate();
        if (!account.getAccountNumber().equals(this.accountNumber)) {
            throw new IllegalArgumentException("Account number mismatch");
        }
        
        try {
            account.withdraw(this.amount);
            System.out.println("Withdrawal successful: $" + String.format("%.2f", amount));
            return true;
        } catch (InsufficientFundsException e) {
            System.err.println("Withdrawal failed: " + e.getMessage());
            throw e;
        }
    }
}