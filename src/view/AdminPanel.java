// File: view/AdminPanel.java
package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AdminPanel extends JFrame {
    private JTextField nameField;
    private JTextField photoPathField;
    private JButton browseButton;
    private JButton addButton;
    private JPanel userListPanel;
    private String adminUsername;
    private JLabel imagePreviewLabel;
    private File selectedImageFile;
    private UserDAO userDAO = new UserDAO();

    public AdminPanel(String username) {
        this.adminUsername = username;

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Couldn't set look and feel");
        }

        ImageIcon icon = new ImageIcon("resources/gym-icon.png");
        setIconImage(icon.getImage());

        setTitle("Admin Panel - Logged in as " + adminUsername);
        setSize(1950, 800);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel welcomeLabel = new JLabel("Welcome, " + adminUsername, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.NORTH);

        // === Side Button Panel ===
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        buttonPanel.setBackground(new Color(230, 240, 250));
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        JButton addUserBtn = new JButton("Add New User");
        JButton markAttendanceBtn = new JButton("Mark Attendance");
        JButton viewHistoryBtn = new JButton("View Attendance History");
        JButton exportBtn = new JButton("Export Attendance to CSV");
        JButton paymentManagementBtn = new JButton("Payment Management");
        JButton logoutBtn = new JButton("Logout");

        addUserBtn.setFont(buttonFont);
        markAttendanceBtn.setFont(buttonFont);
        viewHistoryBtn.setFont(buttonFont);
        exportBtn.setFont(buttonFont);
        paymentManagementBtn.setFont(buttonFont);
        logoutBtn.setFont(buttonFont);

        buttonPanel.add(addUserBtn);
        buttonPanel.add(markAttendanceBtn);
        buttonPanel.add(viewHistoryBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(paymentManagementBtn);
        buttonPanel.add(logoutBtn);
        add(buttonPanel, BorderLayout.WEST);

        // === Center Panel ===
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(new Color(245, 250, 255));

        // === Search Panel ===
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Users"));

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchPanel.add(new JLabel("Search by Name/ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // === Add User Form ===
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));  // Reduced space between components
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Add New User"));

        nameField = new JTextField();
        photoPathField = new JTextField();
        browseButton = new JButton("Browse");
        addButton = new JButton("Add User");
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setPreferredSize(new Dimension(100, 100));  // Smaller image preview size
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Set smaller preferred sizes for input fields and buttons
        nameField.setPreferredSize(new Dimension(120, 20));  // Smaller width and height for name input
        photoPathField.setPreferredSize(new Dimension(120, 20));  // Smaller width and height for photo path input
        browseButton.setPreferredSize(new Dimension(80, 20));  // Smaller size for browse button
        addButton.setPreferredSize(new Dimension(80, 20));      // Smaller size for add button

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Photo Path:"));
        formPanel.add(photoPathField);
        formPanel.add(new JLabel("Preview:"));
        formPanel.add(imagePreviewLabel);
        formPanel.add(browseButton);
        formPanel.add(addButton);

        centerPanel.add(formPanel, BorderLayout.SOUTH);

        // === User List Panel ===
        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(userListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "All Users"));

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // === Button Listeners ===
        addUserBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Use the form below to add users."));
        markAttendanceBtn.addActionListener(e -> new AttendancePanel());
        viewHistoryBtn.addActionListener(e -> new AttendanceHistoryPanel());
        paymentManagementBtn.addActionListener(e -> new PaymentManagementPanel());

        exportBtn.addActionListener(e -> exportAttendanceToCSV());

        logoutBtn.addActionListener(e -> {
            dispose();
            new AdminLogin();
        });

        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Profile Picture");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedImageFile = fileChooser.getSelectedFile();
                photoPathField.setText(selectedImageFile.getAbsolutePath());

                ImageIcon imageIcon = new ImageIcon(selectedImageFile.getAbsolutePath());
                Image img = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imagePreviewLabel.setIcon(new ImageIcon(img));
            }
        });

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String photoPath = photoPathField.getText().trim();

            if (name.isEmpty() || photoPath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and photo are required.");
                return;
            }

            User user = new User(name, photoPath);
            boolean success = userDAO.addUser(user);
            if (success) {
                JOptionPane.showMessageDialog(this, "✅ User added successfully!");
                nameField.setText("");
                photoPathField.setText("");
                imagePreviewLabel.setIcon(null);  // Clear the image preview
                loadUsers();  // Refresh the user list
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to add user.");
            }
        });

        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            List<User> filteredUsers = userDAO.searchUsers(keyword);
            displayUsers(filteredUsers);
        });

        loadUsers();
        setVisible(true);
    }

    private void loadUsers() {
        userListPanel.removeAll();
        List<User> users = userDAO.getAllUsers();
        displayUsers(users);
    }

    private void exportAttendanceToCSV() {
        String filePath = "attendance.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID,Name,Date,Status\n");
            List<User> users = userDAO.getAllUsers();
            for (User user : users) {
                String attendanceStatus = "Present";
                writer.append(user.getId() + "," + user.getName() + ",2025-05-01," + attendanceStatus + "\n");
            }
            JOptionPane.showMessageDialog(this, "✅ Attendance exported to " + filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to export attendance.");
            e.printStackTrace();
        }
    }

    private void displayUsers(List<User> users) {
        userListPanel.removeAll();

        for (User user : users) {
            JPanel userPanel = new JPanel(new BorderLayout());
            userPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel userInfo = new JLabel(user.getId() + ": " + user.getName() + " (" + user.getPhotoPath() + ")");
            userPanel.add(userInfo, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton editBtn = new JButton("Edit");
            JButton deleteBtn = new JButton("Delete");

            // Make buttons smaller
            editBtn.setPreferredSize(new Dimension(70, 20));
            deleteBtn.setPreferredSize(new Dimension(70, 20));

            editBtn.addActionListener(e -> {
                JTextField nameField = new JTextField(user.getName());
                JTextField photoField = new JTextField(user.getPhotoPath());

                JPanel panel = new JPanel(new GridLayout(2, 2));
                panel.add(new JLabel("Name:"));
                panel.add(nameField);
                panel.add(new JLabel("Photo Path:"));
                panel.add(photoField);

                int result = JOptionPane.showConfirmDialog(this, panel, "Edit User", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    user.setName(nameField.getText());
                    user.setPhotoPath(photoField.getText());
                    if (userDAO.updateUser(user)) {
                        JOptionPane.showMessageDialog(this, "✅ User updated.");
                        loadUsers();
                    } else {
                        JOptionPane.showMessageDialog(this, "❌ Failed to update user.");
                    }
                }
            });

            deleteBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (userDAO.deleteUser(user.getId())) {
                        JOptionPane.showMessageDialog(this, "✅ User deleted.");
                        loadUsers();
                    } else {
                        JOptionPane.showMessageDialog(this, "❌ Failed to delete user.");
                    }
                }
            });

            buttonPanel.add(editBtn);
            buttonPanel.add(deleteBtn);
            userPanel.add(buttonPanel, BorderLayout.EAST);

            userListPanel.add(userPanel);
        }

        revalidate();
        repaint();
    }
}
