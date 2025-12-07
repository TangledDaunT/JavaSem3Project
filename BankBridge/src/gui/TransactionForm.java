package gui;

import db.AccountDAO;
import exceptions.DatabaseConnectionException;
import model.*;
import threads.TransactionEngine;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Form for performing transactions
 */
public class TransactionForm extends JDialog {
    private User currentUser;
    private String preSelectedAccount;
    private AccountDAO accountDAO;
    private TransactionEngine transactionEngine;
    private JComboBox<String> accountCombo;
    private JComboBox<String> transactionTypeCombo;
    private JTextField amountField;
    private JComboBox<String> toAccountCombo;
    private JLabel toAccountLabel;
    
    public TransactionForm(JFrame parent, User user, String accountNumber, TransactionEngine engine) {
        super(parent, "Perform Transaction", true);
        this.currentUser = user;
        this.preSelectedAccount = accountNumber;
        this.transactionEngine = engine;
        
        try {
            accountDAO = new AccountDAO();
        } catch (DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this,
                "Database error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(500, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("💸 Transaction");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBounds(170, 20, 200, 30);
        mainPanel.add(titleLabel);
        
        // From Account
        JLabel fromLabel = new JLabel("From Account:");
        fromLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        fromLabel.setBounds(50, 80, 150, 25);
        mainPanel.add(fromLabel);
        
        accountCombo = new JComboBox<>();
        accountCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        accountCombo.setBounds(50, 110, 390, 35);
        loadAccounts();
        mainPanel.add(accountCombo);
        
        // Transaction Type
        JLabel typeLabel = new JLabel("Transaction Type:");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        typeLabel.setBounds(50, 160, 150, 25);
        mainPanel.add(typeLabel);
        
        String[] transactionTypes = {"Deposit", "Withdraw", "Transfer"};
        transactionTypeCombo = new JComboBox<>(transactionTypes);
        transactionTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        transactionTypeCombo.setBounds(50, 190, 390, 35);
        transactionTypeCombo.addActionListener(e -> toggleTransferFields());
        mainPanel.add(transactionTypeCombo);
        
        // Amount
        JLabel amountLabel = new JLabel("Amount ($):");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        amountLabel.setBounds(50, 240, 150, 25);
        mainPanel.add(amountLabel);
        
        amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField.setBounds(50, 270, 390, 35);
        mainPanel.add(amountField);
        
        // To Account (for transfers)
        toAccountLabel = new JLabel("To Account:");
        toAccountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        toAccountLabel.setBounds(50, 320, 150, 25);
        toAccountLabel.setVisible(false);
        mainPanel.add(toAccountLabel);
        
        toAccountCombo = new JComboBox<>();
        toAccountCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        toAccountCombo.setBounds(50, 350, 390, 35);
        toAccountCombo.setVisible(false);
        mainPanel.add(toAccountCombo);
        
        // Buttons
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(100, 405, 130, 35);
        submitButton.setBackground(new Color(46, 204, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(e -> handleTransaction());
        mainPanel.add(submitButton);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(260, 405, 130, 35);
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());
        mainPanel.add(cancelButton);
        
        add(mainPanel);
    }
    
    private void loadAccounts() {
        try {
            List<Account> accounts = accountDAO.findByUserId(currentUser.getUserId());
            
            accountCombo.removeAllItems();
            toAccountCombo.removeAllItems();
            
            for (Account account : accounts) {
                String item = account.getAccountNumber() + " - " + account.getAccountType() + 
                             " ($" + String.format("%.2f", account.getBalance()) + ")";
                accountCombo.addItem(item);
                toAccountCombo.addItem(item);
            }
            
            // Select pre-selected account if provided
            if (preSelectedAccount != null) {
                for (int i = 0; i < accountCombo.getItemCount(); i++) {
                    if (accountCombo.getItemAt(i).startsWith(preSelectedAccount)) {
                        accountCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
        } catch (DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading accounts: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void toggleTransferFields() {
        boolean isTransfer = transactionTypeCombo.getSelectedIndex() == 2;
        toAccountLabel.setVisible(isTransfer);
        toAccountCombo.setVisible(isTransfer);
    }
    
    private void handleTransaction() {
        try {
            // Get selected account number
            String selectedAccount = (String) accountCombo.getSelectedItem();
            if (selectedAccount == null) {
                JOptionPane.showMessageDialog(this,
                    "Please select an account",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String accountNumber = selectedAccount.split(" - ")[0];
            
            // Get amount
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Amount must be greater than zero",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create transaction based on type
            Transaction transaction;
            int transactionType = transactionTypeCombo.getSelectedIndex();
            
            switch (transactionType) {
                case 0: // Deposit
                    transaction = new Deposit(accountNumber, amount);
                    break;
                case 1: // Withdraw
                    transaction = new Withdraw(accountNumber, amount);
                    break;
                case 2: // Transfer
                    String toAccount = (String) toAccountCombo.getSelectedItem();
                    if (toAccount == null) {
                        JOptionPane.showMessageDialog(this,
                            "Please select destination account",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String toAccountNumber = toAccount.split(" - ")[0];
                    
                    if (accountNumber.equals(toAccountNumber)) {
                        JOptionPane.showMessageDialog(this,
                            "Cannot transfer to the same account",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    transaction = new Transfer(accountNumber, toAccountNumber, amount);
                    break;
                default:
                    return;
            }
            
            // Submit to transaction engine
            transactionEngine.submitTransaction(transaction, new TransactionEngine.TransactionCallback() {
                @Override
                public void onSuccess(String message) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(TransactionForm.this,
                            "Transaction completed successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    });
                }
                
                @Override
                public void onFailure(String error) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(TransactionForm.this,
                            "Transaction failed: " + error,
                            "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            });
            
            JOptionPane.showMessageDialog(this,
                "Transaction submitted for processing...",
                "Processing", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid amount",
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}