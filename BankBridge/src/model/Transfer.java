package model;

import exceptions.InsufficientFundsException;

/**
 * Transfer transaction between two accounts
 * Demonstrates complex transaction handling
 */
public class Transfer extends Transaction {
    private String toAccountNumber;
    
    public Transfer(int transactionId, String fromAccount, String toAccount, double amount) {
        super(transactionId, fromAccount, amount, "TRANSFER");
        this.toAccountNumber = toAccount;
        this.description = "Transfer to " + toAccount;
    }
    
    public Transfer(String fromAccount, String toAccount, double amount) {
        super(fromAccount, amount, "TRANSFER");
        this.toAccountNumber = toAccount;
        this.description = "Transfer to " + toAccount;
    }
    
    @Override
    public boolean execute(Account fromAccount) throws Exception {
        validate();
        if (!fromAccount.getAccountNumber().equals(this.accountNumber)) {
            throw new IllegalArgumentException("Source account number mismatch");
        }
        
        try {
            // This only withdraws from source account
            // The deposit to destination should be handled by the DAO layer
            fromAccount.withdraw(this.amount);
            System.out.println("Transfer initiated: $" + String.format("%.2f", amount));
            return true;
        } catch (InsufficientFundsException e) {
            System.err.println("Transfer failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public void validate() throws IllegalArgumentException {
        super.validate();
        if (toAccountNumber == null || toAccountNumber.isEmpty()) {
            throw new IllegalArgumentException("Destination account cannot be empty");
        }
        if (accountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
    }
    
    public String getToAccountNumber() {
        return toAccountNumber;
    }
}