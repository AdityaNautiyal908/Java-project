package dao;

import model.DatabaseConnection;
import model.Payment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private Connection connection;

    public PaymentDAO() {
        try {
            if (!DatabaseConnection.isConnectionValid()) {
                connection = DatabaseConnection.getConnection();
            } else {
                connection = DatabaseConnection.getConnection();
            }
            createPaymentsTableIfNotExists();
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createPaymentsTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS payments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "amount DECIMAL(10,2) NOT NULL, " +
                    "payment_date DATE, " +
                    "due_date DATE NOT NULL, " +
                    "is_paid BOOLEAN DEFAULT FALSE, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id)" +
                    ")";
        
        try {
            if (!DatabaseConnection.isConnectionValid()) {
                connection = DatabaseConnection.getConnection();
            }
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
                System.out.println("Payments table verified/created successfully");
            }
        } catch (SQLException e) {
            System.err.println("Error creating payments table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addPayment(Payment payment) {
        String sql = "INSERT INTO payments (user_id, amount, due_date, is_paid) VALUES (?, ?, ?, ?)";
        try {
            if (!DatabaseConnection.isConnectionValid()) {
                connection = DatabaseConnection.getConnection();
            }
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, payment.getUserId());
                statement.setDouble(2, payment.getAmount());
                statement.setDate(3, Date.valueOf(payment.getDueDate()));
                statement.setBoolean(4, payment.isPaid());
                
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating payment failed, no rows affected.");
                }
                
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        payment.setId(generatedKeys.getInt(1));
                        System.out.println("Payment added successfully with ID: " + payment.getId());
                    } else {
                        throw new SQLException("Creating payment failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding payment: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add payment: " + e.getMessage(), e);
        }
    }

    public void updatePayment(Payment payment) {
        String sql = "UPDATE payments SET amount = ?, payment_date = ?, due_date = ?, is_paid = ? WHERE id = ?";
        try {
            if (!DatabaseConnection.isConnectionValid()) {
                connection = DatabaseConnection.getConnection();
            }
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDouble(1, payment.getAmount());
                statement.setDate(2, payment.getPaymentDate() != null ? Date.valueOf(payment.getPaymentDate()) : null);
                statement.setDate(3, Date.valueOf(payment.getDueDate()));
                statement.setBoolean(4, payment.isPaid());
                statement.setInt(5, payment.getId());
                
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Updating payment failed, no rows affected.");
                }
                System.out.println("Payment updated successfully for ID: " + payment.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error updating payment: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update payment: " + e.getMessage(), e);
        }
    }

    public List<Payment> getPaymentsByUserId(int userId) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE user_id = ?";
        
        try {
            if (!DatabaseConnection.isConnectionValid()) {
                connection = DatabaseConnection.getConnection();
            }
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();
                
                while (resultSet.next()) {
                    Payment payment = new Payment(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("payment_date") != null ? resultSet.getDate("payment_date").toLocalDate() : null,
                        resultSet.getDate("due_date").toLocalDate(),
                        resultSet.getBoolean("is_paid")
                    );
                    payments.add(payment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting payments for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return payments;
    }

    public Payment getCurrentMonthPayment(int userId) {
        String sql = "SELECT * FROM payments WHERE user_id = ? AND MONTH(due_date) = MONTH(CURRENT_DATE()) AND YEAR(due_date) = YEAR(CURRENT_DATE())";
        
        try {
            if (!DatabaseConnection.isConnectionValid()) {
                connection = DatabaseConnection.getConnection();
            }
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();
                
                if (resultSet.next()) {
                    return new Payment(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("payment_date") != null ? resultSet.getDate("payment_date").toLocalDate() : null,
                        resultSet.getDate("due_date").toLocalDate(),
                        resultSet.getBoolean("is_paid")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting current month payment for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    public boolean checkPaymentExists(int userId, LocalDate dueDate) {
        String sql = "SELECT COUNT(*) FROM payments WHERE user_id = ? AND due_date = ?";
        
        try {
            if (!DatabaseConnection.isConnectionValid()) {
                connection = DatabaseConnection.getConnection();
            }
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                statement.setDate(2, Date.valueOf(dueDate));
                
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking payment existence: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
} 