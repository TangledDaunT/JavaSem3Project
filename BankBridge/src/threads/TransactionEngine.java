package threads;

import db.AccountDAO;
import db.TransactionDAO;
import exceptions.DatabaseConnectionException;
import model.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Transaction processing engine using multithreading
 * Implements Runnable for asynchronous transaction processing
 */
public class TransactionEngine implements Runnable {
    private BlockingQueue<TransactionTask> transactionQueue;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private volatile boolean running;
    private Thread engineThread;
    
    public TransactionEngine() throws DatabaseConnectionException {
        this.transactionQueue = new LinkedBlockingQueue<>();
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.running = false;
    }
    
    /**
     * Start the transaction engine
     */
    public void start() {
        if (!running) {
            running = true;
            engineThread = new Thread(this, "TransactionEngine");
            engineThread.start();
            System.out.println("Transaction Engine started.");
        }
    }
    
    /**
     * Stop the transaction engine
     */
    public void stop() {
        running = false;
        if (engineThread != null) {
            engineThread.interrupt();
        }
        System.out.println("Transaction Engine stopped.");
    }
    
    /**
     * Submit a transaction for processing
     */
    public void submitTransaction(Transaction transaction, TransactionCallback callback) {
        try {
            transactionQueue.put(new TransactionTask(transaction, callback));
            System.out.println("Transaction queued: " + transaction.getTransactionType());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Failed to queue transaction: " + e.getMessage());
        }
    }
    
    @Override
    public void run() {
        System.out.println("Transaction Engine thread running...");
        
        while (running) {
            try {
                // Take transaction from queue (blocks if empty)
                TransactionTask task = transactionQueue.take();
                processTransaction(task);
                
            } catch (InterruptedException e) {
                if (!running) {
                    break;
                }
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("Transaction Engine thread terminated.");
    }
    
    /**
     * Process a single transaction
     */
    private void processTransaction(TransactionTask task) {
        Transaction transaction = task.getTransaction();
        TransactionCallback callback = task.getCallback();
        
        try {
            // Get the account
            Account account = accountDAO.findById(transaction.getAccountNumber());
            
            if (account == null) {
                callback.onFailure("Account not found: " + transaction.getAccountNumber());
                return;
            }
            
            // Execute transaction
            boolean success = transaction.execute(account);
            
            if (success) {
                // Update account balance in database
                accountDAO.updateBalance(account.getAccountNumber(), account.getBalance());
                
                // Handle transfer (update destination account)
                if (transaction instanceof Transfer) {
                    Transfer transfer = (Transfer) transaction;
                    Account toAccount = accountDAO.findById(transfer.getToAccountNumber());
                    if (toAccount != null) {
                        toAccount.deposit(transfer.getAmount());
                        accountDAO.updateBalance(toAccount.getAccountNumber(), toAccount.getBalance());
                    }
                }
                
                // Log transaction
                transactionDAO.create(transaction);
                
                callback.onSuccess("Transaction completed successfully");
            } else {
                callback.onFailure("Transaction execution failed");
            }
            
        } catch (Exception e) {
            callback.onFailure("Transaction error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get queue size
     */
    public int getQueueSize() {
        return transactionQueue.size();
    }
    
    /**
     * Callback interface for transaction results
     */
    public interface TransactionCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }
    
    /**
     * Inner class to hold transaction and callback
     */
    private static class TransactionTask {
        private Transaction transaction;
        private TransactionCallback callback;
        
        public TransactionTask(Transaction transaction, TransactionCallback callback) {
            this.transaction = transaction;
            this.callback = callback;
        }
        
        public Transaction getTransaction() {
            return transaction;
        }
        
        public TransactionCallback getCallback() {
            return callback;
        }
    }
}