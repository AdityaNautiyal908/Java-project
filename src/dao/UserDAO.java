// File: dao/UserDAO.java
package dao;

import model.DatabaseConnection;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static model.DatabaseConnection.getConnection;

public class UserDAO {

    public boolean addUser(User user) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO users (name, photo_path) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getPhotoPath());
                int rowsInserted = ps.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add to UserDAO.java

    public boolean updateUser(User user) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET name = ?, photo_path = ? WHERE id = ?")) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPhotoPath());
            stmt.setInt(3, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM users";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String photoPath = rs.getString("photo_path");
                    users.add(new User(id,name, photoPath));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
