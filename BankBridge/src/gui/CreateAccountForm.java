package gui;

import db.AccountDAO;
import db.SecurityUtil;
import exceptions.DatabaseConnectionException;
import model.Account;
import model.SavingsAccount;
import model.CurrentAccount;
import model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Form to create new bank accounts
 */
public class CreateAccountForm extends JDialog {
    private User currentUser;
    private AccountDAO accountDAO;
    private JComboBox<String> accountTypeCombo;
    private JTextField initialBalanceField;
    
    public CreateAccountForm(JFrame parent, User user) {
        super(parent, "Create New Account", true);
        this.currentUser = user;
        
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
        setSize(450, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("🏦 Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(110, 20, 250, 30);
        mainPanel.add(titleLabel);
        
        // Account Type
        JLabel typeLabel = new JLabel("Account Type:");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        typeLabel.setBounds(50, 80, 150, 25);
        mainPanel.add(typeLabel);
        
        String[] accountTypes = {"Savings Account", "Current Account"};
        accountTypeCombo = new JComboBox<>(accountTypes);
        accountTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        accountTypeCombo.setBounds(50, 110, 340, 35);
        accountTypeCombo.addActionListener(e -> updateAccountInfo());
        mainPanel.add(accountTypeCombo);
        
        // Account Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(236, 240, 241));
        infoPanel.setBounds(50, 160, 340, 100);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel infoTitle = new JLabel("Account Features:");
        infoTitle.setFont(new Font("Arial", Font.BOLD, 12));
        infoPanel.add(infoTitle);
        
        JTextArea infoText = new JTextArea();
        infoText.setEditable(false);
        infoText.setBackground(new Color(236, 240, 241));
        infoText.setFont(new Font("Arial", Font.PLAIN, 11));
        infoText.setText("\n• 4% Annual Interest\n• Minimum Balance: $500\n• Unlimited Deposits");
        infoPanel.add(infoText);
        
        mainPanel.add(infoPanel);
        
        // Initial Balance
        JLabel balanceLabel = new JLabel("Initial Balance:");
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        balanceLabel.setBounds(50, 280, 150, 25);
        mainPanel.add(balanceLabel);
        
        initialBalanceField = new JTextField("500.00");
        initialBalanceField.setFont(new Font("Arial", Font.PLAIN, 14));
        initialBalanceField.setBounds(50, 310, 340, 35);
        mainPanel.add(initialBalanceField);
        
        // Buttons
        JButton createButton = new JButton("Create Account");
        createButton.setBounds(80, 360, 140, 35);
        createButton.setBackground(new Color(46, 204, 113));
        createButton.setForeground(Color.WHITE);
        createButton.setFont(new Font("Arial", Font.BOLD, 14));
        createButton.setFocusPainted(false);
        createButton.addActionListener(e -> handleCreateAccount(infoText));
        mainPanel.add(createButton);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(240, 360, 140, 35);
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());
        mainPanel.add(cancelButton);
        
        add(mainPanel);
    }
    
    private void updateAccountInfo() {
        // This would update the info panel based on account type
        // For simplicity, keeping it static for now
    }
    
    private void handleCreateAccount(JTextArea infoText) {
        try {
            double initialBalance = Double.parseDouble(initialBalanceField.getText());
            String accountType = accountTypeCombo.getSelectedIndex() == 0 ? "SAVINGS" : "CURRENT";
            
            // Validate
            if (accountType.equals("SAVINGS") && initialBalance < 500) {
                JOptionPane.showMessageDialog(this,
                    "Savings account requires minimum balance of $500",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (initialBalance < 0) {
                JOptionPane.showMessageDialog(this,
                    "Initial balance cannot be negative",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Generate account number
            String accountNumber = SecurityUtil.generateAccountNumber();
            
            // Create account object
            Account account;
            if (accountType.equals("SAVINGS")) {
                account = new SavingsAccount(accountNumber, currentUser.getUserId(), initialBalance);
                infoText.setText(account.getAccountFeatures());
            } else {
                account = new CurrentAccount(accountNumber, currentUser.getUserId(), initialBalance);
                infoText.setText(account.getAccountFeatures());
            }
            
            // Save to database
            boolean success = accountDAO.create(account);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Account created successfully!\n\n" +
                    "Account Number: " + accountNumber + "\n" +
                    "Type: " + accountType + "\n" +
                    "Initial Balance: $" + String.format("%.2f", initialBalance),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to create account. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
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