package dao;

import model.Attendance;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    private Connection conn;

    // Constructor to ensure the connection is created when needed
    public AttendanceDAO() {
        try {
            conn = DatabaseConnection.getConnection(); // Fetch connection when needed
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ensure the connection is valid before executing
    private void checkConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DatabaseConnection.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean markAttendance(int userId, Date date) {
        checkConnection(); // Ensure connection is valid before performing operations

        if (hasMarkedToday(userId)) {
            System.out.println("⚠️ Attendance already marked for today.");
            return false;
        }

        String sql = "INSERT INTO attendance (user_id, date) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, date);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error marking attendance.");
            e.printStackTrace();
            return false;
        }
    }

    public List<Attendance> getAttendanceForUser(int userId) {
        checkConnection(); // Ensure connection is valid before performing operations

        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Attendance(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDate("date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean hasMarkedToday(int userId) {
        checkConnection(); // Ensure connection is valid before performing operations

        String sql = "SELECT id FROM attendance WHERE user_id = ? AND date = CURDATE()";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
