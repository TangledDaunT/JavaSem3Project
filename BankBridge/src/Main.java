import db.DBConnection;
import db.UserDAO;
import db.SecurityUtil;
import exceptions.DatabaseConnectionException;
import gui.LoginFrame;
import model.User;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main entry point for BankBridge application
 * Initializes database and launches the GUI
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("       🏦 BANKBRIDGE - Banking Management System");
        System.out.println("=".repeat(60));
        System.out.println();
        
        try {
            // Set Look and Feel to system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        try {
            // Initialize database connection
            System.out.println("[1/4] Initializing database connection...");
            DBConnection.getInstance();
            System.out.println("✓ Database connection successful\n");
            
            // Create default admin user if not exists
            System.out.println("[2/4] Checking for default admin user...");
            createDefaultAdmin();
            System.out.println("✓ Admin user check complete\n");
            
            // Create sample users for testing
            System.out.println("[3/4] Creating sample test users...");
            createSampleUsers();
            System.out.println("✓ Sample users created\n");
            
            // Launch GUI
            System.out.println("[4/4] Launching user interface...");
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
            System.out.println("✓ GUI launched successfully\n");
            
            System.out.println("=".repeat(60));
            System.out.println("\n✅ BankBridge is now running!");
            System.out.println("\nDefault Admin Credentials:");
            System.out.println("  Username: admin");
            System.out.println("  Password: admin123\n");
            System.out.println("Sample User Credentials:");
            System.out.println("  Username: john_doe");
            System.out.println("  Password: password123\n");
            System.out.println("=".repeat(60));
            
        } catch (DatabaseConnectionException e) {
            System.err.println("❌ Error: Database connection failed!");
            System.err.println("Details: " + e.getMessage());
            System.err.println("\nPlease ensure:");
            System.err.println("  1. SQLite JDBC driver is in the lib/ folder");
            System.err.println("  2. database/ directory exists");
            System.err.println("  3. You have write permissions\n");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Create default admin user if not exists
     */
    private static void createDefaultAdmin() throws DatabaseConnectionException {
        UserDAO userDAO = new UserDAO();
        
        // Check if admin exists
        User existingAdmin = userDAO.findByUsername("admin");
        if (existingAdmin == null) {
            // Create admin user
            String passwordHash = SecurityUtil.hashPassword("admin123");
            User admin = new User("admin", passwordHash, "System Administrator", "admin@bankbridge.com");
            admin.setAdmin(true);
            
            boolean created = userDAO.create(admin);
            if (created) {
                System.out.println("  ✓ Default admin user created");
                System.out.println("    Username: admin");
                System.out.println("    Password: admin123");
            } else {
                System.out.println("  ⚠️  Failed to create admin user");
            }
        } else {
            System.out.println("  ✓ Admin user already exists");
        }
    }
    
    /**
     * Create sample users for testing
     */
    private static void createSampleUsers() throws DatabaseConnectionException {
        UserDAO userDAO = new UserDAO();
        
        // Sample user 1
        if (userDAO.findByUsername("john_doe") == null) {
            String passwordHash = SecurityUtil.hashPassword("password123");
            User user1 = new User("john_doe", passwordHash, "John Doe", "john@example.com");
            userDAO.create(user1);
            System.out.println("  ✓ Sample user 'john_doe' created (password: password123)");
        }
        
        // Sample user 2
        if (userDAO.findByUsername("jane_smith") == null) {
            String passwordHash = SecurityUtil.hashPassword("password123");
            User user2 = new User("jane_smith", passwordHash, "Jane Smith", "jane@example.com");
            userDAO.create(user2);
            System.out.println("  ✓ Sample user 'jane_smith' created (password: password123)");
        }
    }
}