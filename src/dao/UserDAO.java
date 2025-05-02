// File: dao/UserDAO.java
package dao;

import model.DatabaseConnection;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean addUser(User user) {
        try (Connection conn = DatabaseConnection.getConnection()) {
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

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    String photoPath = rs.getString("photo_path");
                    users.add(new User(name, photoPath));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
