// File: view/AdminPanel.java
package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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

        // Set Nimbus Look and Feel
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

        // ---------- Top Label ----------
        JLabel welcomeLabel = new JLabel("Welcome, " + adminUsername, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.NORTH);

        // ---------- Left Sidebar with Buttons ----------
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

        // ---------- Form Panel (Top Center) ----------
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

        // ---------- Center Panel holds Form + User List ----------
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(new Color(245, 250, 255));

        centerPanel.add(formPanel, BorderLayout.NORTH);

        // User List Panel
        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(userListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "All Users"));

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ---------- Button Actions ----------
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
                if (selectedFile != null) {
                    photoPathField.setText(selectedFile.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(AdminPanel.this, "No file selected!");
                }
            } else {
                JOptionPane.showMessageDialog(AdminPanel.this, "File selection was canceled.");
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
            JLabel label = new JLabel(user.getId() + ": " + user.getName() + " (" + user.getPhotoPath() + ")");
            userListPanel.add(label);
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
