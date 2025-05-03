package view;

import dao.PaymentDAO;
import dao.UserDAO;
import model.Payment;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaymentManagementPanel extends JFrame {
    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private PaymentDAO paymentDAO;
    private UserDAO userDAO;

    public PaymentManagementPanel() {
        paymentDAO = new PaymentDAO();
        userDAO = new UserDAO();

        setTitle("Payment Management");
        setSize(1000, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create table model
        String[] columnNames = {"User ID", "Name", "Amount", "Due Date", "Payment Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table
        paymentTable = new JTable(tableModel);
        paymentTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(paymentTable);

        // Create buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Refresh");
        JButton markPaidButton = new JButton("Mark as Paid");
        JButton addPaymentButton = new JButton("Add New Payment");

        buttonPanel.add(refreshButton);
        buttonPanel.add(markPaidButton);
        buttonPanel.add(addPaymentButton);

        // Add components to frame
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add button listeners
        refreshButton.addActionListener(e -> loadPayments());
        markPaidButton.addActionListener(e -> markSelectedPaymentAsPaid());
        addPaymentButton.addActionListener(e -> showAddPaymentDialog());

        loadPayments();
        setVisible(true);
    }

    private void loadPayments() {
        try {
            tableModel.setRowCount(0); // Clear existing rows
            List<User> users = userDAO.getAllUsers();
            
            for (User user : users) {
                Payment payment = paymentDAO.getCurrentMonthPayment(user.getId());
                Object[] row;
                
                if (payment != null) {
                    // User has a payment record
                    row = new Object[]{
                        user.getId(),
                        user.getName(),
                        payment.getAmount(),
                        payment.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        payment.getPaymentDate() != null ? 
                            payment.getPaymentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "Not Paid",
                        payment.isPaid() ? "Paid" : "Unpaid"
                    };
                } else {
                    // User doesn't have a payment record yet
                    row = new Object[]{
                        user.getId(),
                        user.getName(),
                        "Not Set",
                        "Not Set",
                        "Not Set",
                        "No Payment"
                    };
                }
                tableModel.addRow(row);
            }
            
            // Force table to update
            tableModel.fireTableDataChanged();
            paymentTable.revalidate();
            paymentTable.repaint();
            
            System.out.println("Payments table refreshed successfully");
        } catch (Exception e) {
            System.err.println("Error loading payments: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading payments: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markSelectedPaymentAsPaid() {
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to mark payment.");
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 5);
        
        if (status.equals("No Payment")) {
            // If user doesn't have a payment record, show add payment dialog
            showAddPaymentDialog(userId);
        } else {
            // If user has a payment record, mark it as paid
            Payment payment = paymentDAO.getCurrentMonthPayment(userId);
            if (payment != null) {
                try {
                    payment.setPaid(true);
                    payment.setPaymentDate(LocalDate.now());
                    paymentDAO.updatePayment(payment);
                    
                    // Refresh the table
                    loadPayments();
                    
                    JOptionPane.showMessageDialog(this, "Payment marked as paid successfully!");
                } catch (Exception e) {
                    String errorMessage = "Error updating payment: " + e.getMessage();
                    System.err.println(errorMessage);
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showAddPaymentDialog() {
        showAddPaymentDialog(-1);
    }

    private void showAddPaymentDialog(int userId) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        // User selection
        JComboBox<User> userComboBox = new JComboBox<>();
        List<User> users = userDAO.getAllUsers();
        for (User user : users) {
            userComboBox.addItem(user);
        }
        
        // If userId is provided, preselect that user
        if (userId != -1) {
            for (int i = 0; i < userComboBox.getItemCount(); i++) {
                if (userComboBox.getItemAt(i).getId() == userId) {
                    userComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        // Amount field
        JTextField amountField = new JTextField();
        
        // Due date field
        JTextField dueDateField = new JTextField();
        dueDateField.setText(LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        
        panel.add(new JLabel("User:"));
        panel.add(userComboBox);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        panel.add(dueDateField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Payment", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validate inputs
                if (amountField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter an amount.");
                    return;
                }
                
                if (dueDateField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a due date.");
                    return;
                }
                
                User selectedUser = (User) userComboBox.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                LocalDate dueDate = LocalDate.parse(dueDateField.getText());
                
                // Check if payment already exists for this user and date
                if (paymentDAO.checkPaymentExists(selectedUser.getId(), dueDate)) {
                    JOptionPane.showMessageDialog(this, "A payment already exists for this user on the selected date.");
                    return;
                }
                
                Payment payment = new Payment(selectedUser.getId(), amount, dueDate);
                paymentDAO.addPayment(payment);
                
                // Refresh the table
                loadPayments();
                
                JOptionPane.showMessageDialog(this, "Payment added successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount (numbers only).");
            } catch (java.time.format.DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid date in YYYY-MM-DD format.");
            } catch (Exception e) {
                String errorMessage = "Error adding payment: " + e.getMessage();
                System.err.println(errorMessage);
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 