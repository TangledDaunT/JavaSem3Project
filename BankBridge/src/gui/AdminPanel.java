package gui;

import db.AccountDAO;
import db.UserDAO;
import db.TransactionDAO;
import exceptions.DatabaseConnectionException;
import model.Account;
import model.User;
import model.Transaction;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Admin panel to view all users, accounts, and transactions
 */
public class AdminPanel extends JFrame {
    private User adminUser;
    private UserDAO userDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private JTabbedPane tabbedPane;
    
    public AdminPanel(User admin) {
        this.adminUser = admin;
        
        if (!admin.isAdmin()) {
            JOptionPane.showMessageDialog(this,
                "Access Denied: Admin privileges required",
                "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        try {
            userDAO = new UserDAO();
            accountDAO = new AccountDAO();
            transactionDAO = new TransactionDAO();
        } catch (DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this,
                "Database error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("🔑 BankBridge - Admin Panel");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 126, 34));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("🔑 Admin Control Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel adminLabel = new JLabel("Admin: " + adminUser.getFullName());
        adminLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        adminLabel.setForeground(Color.WHITE);
        headerPanel.add(adminLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Add tabs
        tabbedPane.addTab("👥 Users", createUsersPanel());
        tabbedPane.addTab("🏦 Accounts", createAccountsPanel());
        tabbedPane.addTab("💸 Transactions", createTransactionsPanel());
        tabbedPane.addTab("📊 Statistics", createStatisticsPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Bottom panel with refresh button
        JPanel bottomPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh All");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> refreshAllData());
        bottomPanel.add(refreshButton);
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(231, 76, 60));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(closeButton);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Load initial data
        refreshAllData();
    }
    
    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"ID", "Username", "Full Name", "Email", "Role", "Created At"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Store reference for refresh
        panel.putClientProperty("table", table);
        panel.putClientProperty("model", model);
        
        return panel;
    }
    
    private JPanel createAccountsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Account Number", "User ID", "Type", "Balance", "Status", "Created"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        panel.putClientProperty("table", table);
        panel.putClientProperty("model", model);
        
        return panel;
    }
    
    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"ID", "Account", "Type", "Amount", "To Account", "Timestamp"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        panel.putClientProperty("table", table);
        panel.putClientProperty("model", model);
        
        return panel;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextArea statsArea = new JTextArea();
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        statsArea.setEditable(false);
        statsArea.setText("Loading statistics...");
        
        panel.add(new JScrollPane(statsArea));
        panel.putClientProperty("statsArea", statsArea);
        
        return panel;
    }
    
    private void refreshAllData() {
        try {
            // Refresh Users
            JPanel usersPanel = (JPanel) tabbedPane.getComponentAt(0);
            DefaultTableModel usersModel = (DefaultTableModel) usersPanel.getClientProperty("model");
            usersModel.setRowCount(0);
            
            List<User> users = userDAO.findAll();
            for (User user : users) {
                Object[] row = {
                    user.getUserId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    user.isAdmin() ? "Admin" : "User",
                    user.getCreatedAt().toString().substring(0, 10)
                };
                usersModel.addRow(row);
            }
            
            // Refresh Accounts
            JPanel accountsPanel = (JPanel) tabbedPane.getComponentAt(1);
            DefaultTableModel accountsModel = (DefaultTableModel) accountsPanel.getClientProperty("model");
            accountsModel.setRowCount(0);
            
            List<Account> accounts = accountDAO.findAll();
            double totalBalance = 0;
            for (Account account : accounts) {
                totalBalance += account.getBalance();
                Object[] row = {
                    account.getAccountNumber(),
                    account.getUserId(),
                    account.getAccountType(),
                    String.format("$%.2f", account.getBalance()),
                    account.isActive() ? "Active" : "Inactive",
                    account.getCreatedAt().toString().substring(0, 10)
                };
                accountsModel.addRow(row);
            }
            
            // Refresh Transactions
            JPanel transactionsPanel = (JPanel) tabbedPane.getComponentAt(2);
            DefaultTableModel transactionsModel = (DefaultTableModel) transactionsPanel.getClientProperty("model");
            transactionsModel.setRowCount(0);
            
            List<Transaction> transactions = transactionDAO.findAll();
            for (Transaction t : transactions) {
                Object[] row = {
                    t.getTransactionId(),
                    t.getAccountNumber(),
                    t.getTransactionType(),
                    String.format("$%.2f", t.getAmount()),
                    t instanceof model.Transfer ? ((model.Transfer) t).getToAccountNumber() : "-",
                    t.getTimestamp().toString().substring(0, 19).replace("T", " ")
                };
                transactionsModel.addRow(row);
            }
            
            // Update Statistics
            JPanel statsPanel = (JPanel) tabbedPane.getComponentAt(3);
            JTextArea statsArea = (JTextArea) statsPanel.getClientProperty("statsArea");
            
            StringBuilder stats = new StringBuilder();
            stats.append("\n========================================\n");
            stats.append("      BANKBRIDGE SYSTEM STATISTICS      \n");
            stats.append("========================================\n\n");
            stats.append("Total Users:        ").append(users.size()).append("\n");
            stats.append("Total Accounts:     ").append(accounts.size()).append("\n");
            stats.append("Total Transactions: ").append(transactions.size()).append("\n");
            stats.append("Total Balance:      $").append(String.format("%.2f", totalBalance)).append("\n\n");
            stats.append("Active Accounts:    ").append(
                accounts.stream().filter(Account::isActive).count()
            ).append("\n");
            stats.append("Admin Users:        ").append(
                users.stream().filter(User::isAdmin).count()
            ).append("\n\n");
            stats.append("========================================\n");
            
            statsArea.setText(stats.toString());
            
            JOptionPane.showMessageDialog(this,
                "Data refreshed successfully",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this,
                "Error refreshing data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}