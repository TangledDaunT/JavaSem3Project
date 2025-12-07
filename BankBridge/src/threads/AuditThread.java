package threads;

import db.AccountDAO;
import db.TransactionDAO;
import exceptions.DatabaseConnectionException;
import model.Account;
import model.Transaction;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Background audit thread for monitoring and logging
 * Extends Thread class for continuous monitoring
 */
public class AuditThread extends Thread {
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private volatile boolean running;
    private int auditInterval; // in seconds
    
    public AuditThread(int auditInterval) throws DatabaseConnectionException {
        super("AuditThread");
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.auditInterval = auditInterval;
        this.running = false;
        setDaemon(true); // Daemon thread - won't prevent JVM shutdown
    }
    
    /**
     * Start the audit thread
     */
    public void startAudit() {
        if (!running) {
            running = true;
            start();
            System.out.println("Audit Thread started with " + auditInterval + "s interval.");
        }
    }
    
    /**
     * Stop the audit thread
     */
    public void stopAudit() {
        running = false;
        interrupt();
        System.out.println("Audit Thread stopped.");
    }
    
    @Override
    public void run() {
        System.out.println("Audit Thread is running...");
        
        while (running) {
            try {
                performAudit();
                
                // Sleep for the specified interval
                Thread.sleep(auditInterval * 1000L);
                
            } catch (InterruptedException e) {
                if (!running) {
                    break;
                }
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Audit error: " + e.getMessage());
            }
        }
        
        System.out.println("Audit Thread terminated.");
    }
    
    /**
     * Perform audit operations
     */
    private void performAudit() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = LocalDateTime.now().format(formatter);
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("AUDIT REPORT - " + timestamp);
            System.out.println("=".repeat(60));
            
            // Get all accounts
            List<Account> accounts = accountDAO.findAll();
            double totalBalance = 0.0;
            int activeAccounts = 0;
            
            System.out.println("\nTotal Accounts: " + accounts.size());
            
            for (Account account : accounts) {
                if (account.isActive()) {
                    activeAccounts++;
                    totalBalance += account.getBalance();
                }
            }
            
            System.out.println("Active Accounts: " + activeAccounts);
            System.out.println("Total System Balance: $" + String.format("%.2f", totalBalance));
            
            // Get recent transactions
            List<Transaction> recentTransactions = transactionDAO.findAll();
            if (recentTransactions.size() > 0) {
                System.out.println("\nTotal Transactions: " + recentTransactions.size());
                System.out.println("Recent Transactions (last 5):");
                
                int count = 0;
                for (Transaction t : recentTransactions) {
                    if (count >= 5) break;
                    System.out.println("  - " + t.getDisplayInfo());
                    count++;
                }
            } else {
                System.out.println("\nNo transactions recorded.");
            }
            
            // Check for suspicious activity (large transactions)
            checkSuspiciousActivity(recentTransactions);
            
            System.out.println("=".repeat(60) + "\n");
            
        } catch (DatabaseConnectionException e) {
            System.err.println("Audit failed: " + e.getMessage());
        }
    }
    
    /**
     * Check for suspicious transactions
     */
    private void checkSuspiciousActivity(List<Transaction> transactions) {
        final double SUSPICIOUS_THRESHOLD = 10000.0;
        boolean suspiciousFound = false;
        
        System.out.println("\nSecurity Check:");
        
        for (Transaction t : transactions) {
            if (t.getAmount() > SUSPICIOUS_THRESHOLD) {
                if (!suspiciousFound) {
                    System.out.println("  ⚠️  Large transactions detected:");
                    suspiciousFound = true;
                }
                System.out.println("    - " + t.getTransactionType() + ": $" + 
                                 String.format("%.2f", t.getAmount()) + 
                                 " (Account: " + t.getAccountNumber() + ")");
            }
        }
        
        if (!suspiciousFound) {
            System.out.println("  ✓ No suspicious activity detected.");
        }
    }
    
    /**
     * Change audit interval
     */
    public void setAuditInterval(int seconds) {
        this.auditInterval = seconds;
        System.out.println("Audit interval changed to " + seconds + " seconds.");
    }
}