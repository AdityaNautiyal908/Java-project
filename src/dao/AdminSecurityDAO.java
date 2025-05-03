package dao;

import model.AdminSecurity;
import model.DatabaseConnection;

import java.sql.*;

public class AdminSecurityDAO {
    private static Connection connection;

    public AdminSecurityDAO() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DatabaseConnection.getConnection();
            }
            createAdminSecurityTable();
        } catch (SQLException e) {
            System.err.println("Error initializing AdminSecurityDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createAdminSecurityTable() {
        // First check if the admin table exists
        String checkAdminTable = "SHOW TABLES LIKE 'admin'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkAdminTable)) {
            
            if (!rs.next()) {
                // Create admin table if it doesn't exist
                String createAdminTable = "CREATE TABLE admin (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "username VARCHAR(50) NOT NULL UNIQUE, " +
                    "password VARCHAR(50) NOT NULL" +
                    ")";
                stmt.execute(createAdminTable);
                System.out.println("Admin table created successfully");
            }
        } catch (SQLException e) {
            System.err.println("Error checking/creating admin table: " + e.getMessage());
            e.printStackTrace();
        }

        // Now create the admin_security table
        String sql = "CREATE TABLE IF NOT EXISTS admin_security (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL UNIQUE, " +
                    "security_question VARCHAR(255) NOT NULL, " +
                    "security_answer VARCHAR(255) NOT NULL, " +
                    "pet_name VARCHAR(100) NOT NULL, " +
                    "secret_code VARCHAR(100) NOT NULL, " +
                    "FOREIGN KEY (username) REFERENCES admin(username) ON DELETE CASCADE" +
                    ")";
        
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Admin security table created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating admin security table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean addSecurityInfo(AdminSecurity adminSecurity) {
        String sql = "INSERT INTO admin_security (username, security_question, security_answer, pet_name, secret_code) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try {
            if (connection == null || connection.isClosed()) {
                connection = DatabaseConnection.getConnection();
            }
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, adminSecurity.getUsername());
                statement.setString(2, adminSecurity.getSecurityQuestion());
                statement.setString(3, adminSecurity.getSecurityAnswer());
                statement.setString(4, adminSecurity.getPetName());
                statement.setString(5, adminSecurity.getSecretCode());
                
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Security info added successfully for user: " + adminSecurity.getUsername());
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding security info: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public AdminSecurity getSecurityInfo(String username) {
        String sql = "SELECT * FROM admin_security WHERE username = ?";
        
        try {
            if (connection == null || connection.isClosed()) {
                connection = DatabaseConnection.getConnection();
            }
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                
                if (rs.next()) {
                    AdminSecurity adminSecurity = new AdminSecurity();
                    adminSecurity.setId(rs.getInt("id"));
                    adminSecurity.setUsername(rs.getString("username"));
                    adminSecurity.setSecurityQuestion(rs.getString("security_question"));
                    adminSecurity.setSecurityAnswer(rs.getString("security_answer"));
                    adminSecurity.setPetName(rs.getString("pet_name"));
                    adminSecurity.setSecretCode(rs.getString("secret_code"));
                    return adminSecurity;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting security info: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean verifySecurityAnswer(String username, String answer) {
        String sql = "SELECT security_answer FROM admin_security WHERE username = ?";
        
        try {
            if (connection == null || connection.isClosed()) {
                connection = DatabaseConnection.getConnection();
            }
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                
                if (rs.next()) {
                    return rs.getString("security_answer").equals(answer);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error verifying security answer: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifyPetName(String username, String petName) {
        String sql = "SELECT pet_name FROM admin_security WHERE username = ?";
        
        try {
            if (connection == null || connection.isClosed()) {
                connection = DatabaseConnection.getConnection();
            }
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                
                if (rs.next()) {
                    return rs.getString("pet_name").equals(petName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error verifying pet name: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifySecretCode(String username, String secretCode) {
        String sql = "SELECT secret_code FROM admin_security WHERE username = ?";
        
        try {
            if (connection == null || connection.isClosed()) {
                connection = DatabaseConnection.getConnection();
            }
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                
                if (rs.next()) {
                    return rs.getString("secret_code").equals(secretCode);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error verifying secret code: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
} 