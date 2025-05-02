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
        setSize(800, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel welcomeLabel = new JLabel("Welcome, " + adminUsername, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        buttonPanel.setBackground(new Color(230, 240, 250));

        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        JButton addUserBtn = new JButton("Add New User");
        JButton markAttendanceBtn = new JButton("Mark Attendance");
        JButton viewHistoryBtn = new JButton("View Attendance History");
        JButton exportBtn = new JButton("Export Attendance to CSV");
        JButton logoutBtn = new JButton("Logout");

        addUserBtn.setFont(buttonFont);
        markAttendanceBtn.setFont(buttonFont);
        viewHistoryBtn.setFont(buttonFont);
        exportBtn.setFont(buttonFont);
        logoutBtn.setFont(buttonFont);

        buttonPanel.add(addUserBtn);
        buttonPanel.add(markAttendanceBtn);
        buttonPanel.add(viewHistoryBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(logoutBtn);

        add(buttonPanel, BorderLayout.WEST);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Add New User"));

        nameField = new JTextField();
        photoPathField = new JTextField();
        browseButton = new JButton("Browse");
        addButton = new JButton("Add User");

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Photo Path:"));
        formPanel.add(photoPathField);
        formPanel.add(browseButton);
        formPanel.add(addButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(new Color(245, 250, 255));

        centerPanel.add(formPanel, BorderLayout.NORTH);

        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(userListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "All Users"));

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        addUserBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Use the form above to add users."));
        markAttendanceBtn.addActionListener(e -> new AttendancePanel());
        viewHistoryBtn.addActionListener(e -> new AttendanceHistoryPanel());

        exportBtn.addActionListener(e -> exportAttendanceToCSV());

        logoutBtn.addActionListener(e -> {
            dispose();
            new AdminLogin();
        });

        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(AdminPanel.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                photoPathField.setText(selectedFile.getAbsolutePath());
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
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to add user.");
            }
        });

        loadUsers();
        setVisible(true);
    }

    private void loadUsers() {
        userListPanel.removeAll();
        List<User> users = userDAO.getAllUsers();

        for (User user : users) {
            JPanel userPanel = new JPanel(new BorderLayout());
            userPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel userInfo = new JLabel(user.getId() + ": " + user.getName() + " (" + user.getPhotoPath() + ")");
            userPanel.add(userInfo, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton editBtn = new JButton("Edit");
            JButton deleteBtn = new JButton("Delete");

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

        userListPanel.revalidate();
        userListPanel.repaint();
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
}
