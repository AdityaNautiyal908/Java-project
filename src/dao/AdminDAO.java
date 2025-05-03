// File: dao/AdminDAO.java
package dao;

import model.DatabaseConnection;
import java.sql.*;

public class AdminDAO {
    public boolean isValidAdmin(String username, String password) {
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Admin login error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean createAdmin(String username, String password) {
        String query = "INSERT INTO admin (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Admin account created successfully for user: " + username);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Admin creation error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(String username, String newPassword) {
        String query = "UPDATE admin SET password = ? WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Password updated successfully for user: " + username);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Password update error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
