package model;

/**
 * Current Account with overdraft facility
 * Demonstrates Inheritance and Polymorphism
 */
public class CurrentAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 1000.0;
    private static final double MINIMUM_BALANCE = -1000.0; // Can go negative
    
    public CurrentAccount(String accountNumber, int userId, double balance) {
        super(accountNumber, userId, balance, "CURRENT");
    }
    
    public CurrentAccount(String accountNumber, int userId) {
        super(accountNumber, userId, "CURRENT", 0.0);
    }
    
    @Override
    public void applyInterest() {
        // Current accounts don't earn interest
        // But may charge fees if balance is negative
        if (this.balance < 0) {
            double overdraftFee = Math.abs(this.balance) * 0.02; // 2% fee
            this.balance -= overdraftFee;
            System.out.println("Overdraft fee charged: $" + String.format("%.2f", overdraftFee));
        }
    }
    
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
    
    @Override
    public String getAccountFeatures() {
        return "Current Account Features:\n" +
               "- No Interest\n" +
               "- Overdraft Limit: $" + OVERDRAFT_LIMIT + "\n" +
               "- Unlimited Transactions\n" +
               "- Business Friendly";
    }
    
    public static double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }
}