package gui;

import db.TransactionDAO;
import exceptions.DatabaseConnectionException;
import model.Transaction;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Dialog to show transaction history
 */
public class TransactionHistoryDialog extends JDialog {
    private String accountNumber;
    private TransactionDAO transactionDAO;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    
    public TransactionHistoryDialog(JFrame parent, String accountNumber) {
        super(parent, "Transaction History", true);
        this.accountNumber = accountNumber;
        
        try {
            transactionDAO = new TransactionDAO();
        } catch (DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this,
                "Database error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        initializeUI();
        loadTransactions();
    }
    
    private void initializeUI() {
        setSize(800, 500);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("📊 Transaction History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel accountLabel = new JLabel("Account: " + accountNumber);
        accountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        accountLabel.setForeground(Color.WHITE);
        headerPanel.add(accountLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Type", "Amount", "To/From", "Date & Time", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        transactionTable = new JTable(tableModel);
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 13));
        transactionTable.setRowHeight(25);
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        transactionTable.getTableHeader().setBackground(new Color(52, 152, 219));
        transactionTable.getTableHeader().setForeground(Color.WHITE);
        transactionTable.setSelectionBackground(new Color(174, 214, 241));
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Close button
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(231, 76, 60));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadTransactions() {
        try {
            List<Transaction> transactions = transactionDAO.findByAccountNumber(accountNumber);
            
            tableModel.setRowCount(0);
            
            for (Transaction t : transactions) {
                Object[] row = {
                    t.getTransactionId(),
                    t.getTransactionType(),
                    String.format("$%.2f", t.getAmount()),
                    t instanceof model.Transfer ? ((model.Transfer) t).getToAccountNumber() : "-",
                    t.getTimestamp().toString().substring(0, 19).replace("T", " "),
                    t.getDescription() != null ? t.getDescription() : "-"
                };
                tableModel.addRow(row);
            }
            
            if (transactions.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No transactions found for this account",
                    "No Transactions", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading transactions: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}