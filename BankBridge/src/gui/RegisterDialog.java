package gui;

import db.UserDAO;
import db.SecurityUtil;
import exceptions.DatabaseConnectionException;
import model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Registration dialog for new users
 */
public class RegisterDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private UserDAO userDAO;
    
    public RegisterDialog(JFrame parent, UserDAO userDAO) {
        super(parent, "Register New User", true);
        this.userDAO = userDAO;
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(400, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(110, 20, 200, 30);
        mainPanel.add(titleLabel);
        
        // Full Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(30, 70, 100, 25);
        mainPanel.add(nameLabel);
        
        fullNameField = new JTextField();
        fullNameField.setBounds(30, 95, 340, 30);
        mainPanel.add(fullNameField);
        
        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 135, 100, 25);
        mainPanel.add(emailLabel);
        
        emailField = new JTextField();
        emailField.setBounds(30, 160, 340, 30);
        mainPanel.add(emailField);
        
        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 200, 100, 25);
        mainPanel.add(usernameLabel);
        
        usernameField = new JTextField();
        usernameField.setBounds(30, 225, 340, 30);
        mainPanel.add(usernameField);
        
        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 265, 100, 25);
        mainPanel.add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(30, 290, 340, 30);
        mainPanel.add(passwordField);
        
        // Confirm Password
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setBounds(30, 330, 150, 25);
        mainPanel.add(confirmLabel);
        
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(30, 355, 340, 30);
        mainPanel.add(confirmPasswordField);
        
        // Buttons
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(80, 400, 110, 35);
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> handleRegistration());
        mainPanel.add(registerButton);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(210, 400, 110, 35);
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());
        mainPanel.add(cancelButton);
        
        add(mainPanel);
    }
    
    private void handleRegistration() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validation
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all required fields",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this,
                "Username must be at least 3 characters",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 6 characters",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Check if username exists
            if (userDAO.findByUsername(username) != null) {
                JOptionPane.showMessageDialog(this,
                    "Username already exists. Please choose another.",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create new user
            String passwordHash = SecurityUtil.hashPassword(password);
            User newUser = new User(username, passwordHash, fullName, email);
            
            boolean success = userDAO.create(newUser);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Registration successful! You can now login.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Registration failed. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this,
                "Database error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}