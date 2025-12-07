package gui;

import db.AccountDAO;
import exceptions.DatabaseConnectionException;
import model.Account;
import model.User;
import threads.AuditThread;
import threads.TransactionEngine;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Main dashboard after login
 */
public class Dashboard extends JFrame {
    private User currentUser;
    private AccountDAO accountDAO;
    private JTable accountsTable;
    private DefaultTableModel tableModel;
    private TransactionEngine transactionEngine;
    private AuditThread auditThread;
    
    public Dashboard(User user) {
        this.currentUser = user;
        
        try {
            accountDAO = new AccountDAO();
            
            // Start background threads
            transactionEngine = new TransactionEngine();
            transactionEngine.start();
            
            // Start audit thread (every 60 seconds)
            auditThread = new AuditThread(60);
            auditThread.startAudit();
            
        } catch (DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this,
                "Database connection failed: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        initializeUI();
        loadAccounts();
    }
    
    private void initializeUI() {
        setTitle("BankBridge - Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(236, 240, 241));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Center Panel with accounts table
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel welcomeLabel = new JLabel("👋 Welcome, " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JLabel roleLabel = new JLabel(currentUser.isAdmin() ? "🔑 Admin" : "👤 User");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        roleLabel.setForeground(Color.WHITE);
        headerPanel.add(roleLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel("My Accounts");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Accounts Table
        String[] columns = {"Account Number", "Type", "Balance", "Status", "Created"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        accountsTable = new JTable(tableModel);
        accountsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        accountsTable.setRowHeight(30);
        accountsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        accountsTable.getTableHeader().setBackground(new Color(52, 152, 219));
        accountsTable.getTableHeader().setForeground(Color.WHITE);
        accountsTable.setSelectionBackground(new Color(174, 214, 241));
        
        JScrollPane scrollPane = new JScrollPane(accountsTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(236, 240, 241));
        
        // Create Account Button
        JButton createAccountBtn = createStyledButton("Create Account", new Color(46, 204, 113));
        createAccountBtn.addActionListener(e -> openCreateAccountForm());
        buttonPanel.add(createAccountBtn);
        
        // Transaction Button
        JButton transactionBtn = createStyledButton("Transactions", new Color(52, 152, 219));
        transactionBtn.addActionListener(e -> openTransactionForm());
        buttonPanel.add(transactionBtn);
        
        // View Transactions Button
        JButton viewTransactionsBtn = createStyledButton("View History", new Color(155, 89, 182));
        viewTransactionsBtn.addActionListener(e -> openTransactionHistory());
        buttonPanel.add(viewTransactionsBtn);
        
        // Admin Panel Button (only for admins)
        if (currentUser.isAdmin()) {
            JButton adminBtn = createStyledButton("Admin Panel", new Color(230, 126, 34));
            adminBtn.addActionListener(e -> openAdminPanel());
            buttonPanel.add(adminBtn);
        }
        
        // Refresh Button
        JButton refreshBtn = createStyledButton("Refresh", new Color(127, 140, 141));
        refreshBtn.addActionListener(e -> loadAccounts());
        buttonPanel.add(refreshBtn);
        
        // Logout Button
        JButton logoutBtn = createStyledButton("Logout", new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> handleLogout());
        buttonPanel.add(logoutBtn);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void loadAccounts() {
        try {
            List<Account> accounts = accountDAO.findByUserId(currentUser.getUserId());
            
            // Clear table
            tableModel.setRowCount(0);
            
            // Populate table
            for (Account account : accounts) {
                Object[] row = {
                    account.getAccountNumber(),
                    account.getAccountType(),
                    String.format("$%.2f", account.getBalance()),
                    account.isActive() ? "Active" : "Inactive",
                    account.getCreatedAt().toString().substring(0, 10)
                };
                tableModel.addRow(row);
            }
            
            if (accounts.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "You don't have any accounts yet. Create one to get started!",
                    "No Accounts", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading accounts: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openCreateAccountForm() {
        CreateAccountForm form = new CreateAccountForm(this, currentUser);
        form.setVisible(true);
        loadAccounts(); // Refresh after closing
    }
    
    private void openTransactionForm() {
        int selectedRow = accountsTable.getSelectedRow();
        String accountNumber = null;
        
        if (selectedRow >= 0) {
            accountNumber = (String) tableModel.getValueAt(selectedRow, 0);
        }
        
        TransactionForm form = new TransactionForm(this, currentUser, accountNumber, transactionEngine);
        form.setVisible(true);
        loadAccounts(); // Refresh after closing
    }
    
    private void openTransactionHistory() {
        int selectedRow = accountsTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select an account first",
                "No Account Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String accountNumber = (String) tableModel.getValueAt(selectedRow, 0);
        TransactionHistoryDialog dialog = new TransactionHistoryDialog(this, accountNumber);
        dialog.setVisible(true);
    }
    
    private void openAdminPanel() {
        AdminPanel adminPanel = new AdminPanel(currentUser);
        adminPanel.setVisible(true);
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Stop background threads
            if (transactionEngine != null) {
                transactionEngine.stop();
            }
            if (auditThread != null) {
                auditThread.stopAudit();
            }
            
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        }
    }
}