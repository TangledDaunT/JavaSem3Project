package model;

/**
 * Savings Account with interest calculation
 * Demonstrates Inheritance and Method Overriding
 */
public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.04; // 4% annual
    private static final double MINIMUM_BALANCE = 500.0;
    
    public SavingsAccount(String accountNumber, int userId, double balance) {
        super(accountNumber, userId, balance, "SAVINGS");
    }
    
    public SavingsAccount(String accountNumber, int userId) {
        super(accountNumber, userId, "SAVINGS", MINIMUM_BALANCE);
    }
    
    @Override
    public void applyInterest() {
        // Calculate and add monthly interest
        double interest = this.balance * (INTEREST_RATE / 12);
        this.balance += interest;
        System.out.println("Interest applied: $" + String.format("%.2f", interest));
    }
    
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
    
    @Override
    public String getAccountFeatures() {
        return "Savings Account Features:\n" +
               "- 4% Annual Interest\n" +
               "- Minimum Balance: $" + MINIMUM_BALANCE + "\n" +
               "- Unlimited Deposits\n" +
               "- Limited Withdrawals";
    }
    
    public static double getInterestRate() {
        return INTEREST_RATE;
    }
}