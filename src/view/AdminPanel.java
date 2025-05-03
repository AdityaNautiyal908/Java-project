// File: view/AdminPanel.java
package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
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
        
        // Basic frame setup
        setTitle("Fitness Hub - Admin Panel");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(41, 128, 185), // Dark blue
                    0, getHeight(), new Color(44, 62, 80) // Darker blue
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create content panel
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Make sure the frame is focusable
        setFocusable(true);

        // Load users
        loadUsers();
        
        // Show the frame
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + adminUsername);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        // Action buttons panel
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionButtonsPanel.setOpaque(false);

        // Mark Attendance button
        JButton markAttendanceBtn = createStyledButton("Mark Attendance", new Color(52, 152, 219));
        markAttendanceBtn.addActionListener(e -> new AttendancePanel());
        actionButtonsPanel.add(markAttendanceBtn);

        // View History button
        JButton viewHistoryBtn = createStyledButton("View History", new Color(155, 89, 182));
        viewHistoryBtn.addActionListener(e -> new AttendanceHistoryPanel());
        actionButtonsPanel.add(viewHistoryBtn);

        // Export CSV button
        JButton exportBtn = createStyledButton("Export CSV", new Color(230, 126, 34));
        exportBtn.addActionListener(e -> exportAttendanceToCSV());
        actionButtonsPanel.add(exportBtn);

        // Payment Management button
        JButton paymentBtn = createStyledButton("Payments", new Color(39, 174, 96));
        paymentBtn.addActionListener(e -> new PaymentManagementPanel());
        actionButtonsPanel.add(paymentBtn);

        // Logout button
        JButton logoutButton = createStyledButton("Logout", new Color(255, 87, 87));
        logoutButton.addActionListener(e -> {
            dispose();
            new AdminLogin();
        });
        actionButtonsPanel.add(logoutButton);

        headerPanel.add(actionButtonsPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(false);

        // Create form panel
        JPanel formPanel = createFormPanel();
        contentPanel.add(formPanel, BorderLayout.NORTH);

        // Create user list panel
        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(userListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100)),
            "All Users",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 16),
            Color.WHITE
        ));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100)),
            "Add New User",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 16),
            Color.WHITE
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = createStyledTextField();
        formPanel.add(nameField, gbc);

        // Photo path field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel photoLabel = new JLabel("Photo Path:");
        photoLabel.setForeground(Color.WHITE);
        photoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(photoLabel, gbc);

        gbc.gridx = 1;
        photoPathField = createStyledTextField();
        formPanel.add(photoPathField, gbc);

        // Browse button
        gbc.gridx = 2;
        browseButton = createStyledButton("Browse", new Color(46, 204, 113));
        browseButton.addActionListener(e -> browseForImage());
        formPanel.add(browseButton, gbc);

        // Image preview
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setPreferredSize(new Dimension(150, 150));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100)));
        imagePreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(imagePreviewLabel, gbc);

        // Add button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        addButton = createStyledButton("Add User", new Color(46, 204, 113));
        addButton.addActionListener(e -> addUser());
        formPanel.add(addButton, gbc);

        // Add keyboard navigation
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    photoPathField.requestFocus();
                }
            }
        });

        photoPathField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addUser();
                }
            }
        });

        return formPanel;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setBackground(new Color(255, 255, 255, 200));
        textField.setForeground(Color.BLACK);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.selectAll();
                textField.setBackground(new Color(255, 255, 255, 255));
            }
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBackground(new Color(255, 255, 255, 200));
            }
        });
        return textField;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private void browseForImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            photoPathField.setText(selectedImageFile.getAbsolutePath());

            ImageIcon imageIcon = new ImageIcon(selectedImageFile.getAbsolutePath());
            Image img = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imagePreviewLabel.setIcon(new ImageIcon(img));
            
            addButton.requestFocus();
        }
    }

    private void addUser() {
        String name = nameField.getText().trim();
        String photoPath = photoPathField.getText().trim();

        if (name.isEmpty() || photoPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and photo are required.");
            nameField.requestFocus();
            return;
        }

        User user = new User(name, photoPath);
        boolean success = userDAO.addUser(user);
        if (success) {
            JOptionPane.showMessageDialog(this, "✅ User added successfully!");
            nameField.setText("");
            photoPathField.setText("");
            imagePreviewLabel.setIcon(null);
            loadUsers();
            nameField.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to add user.");
            nameField.requestFocus();
        }
    }

    private void loadUsers() {
        userListPanel.removeAll();
        List<User> users = userDAO.getAllUsers();
        displayUsers(users);
    }

    private void displayUsers(List<User> users) {
        userListPanel.removeAll();

        for (User user : users) {
            JPanel userPanel = new JPanel(new BorderLayout());
            userPanel.setOpaque(false);
            userPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // User info panel
            JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            infoPanel.setOpaque(false);
            
            JLabel userInfo = new JLabel(user.getId() + ": " + user.getName() + " (" + user.getPhotoPath() + ")");
            userInfo.setForeground(Color.WHITE);
            userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            infoPanel.add(userInfo);
            
            userPanel.add(infoPanel, BorderLayout.CENTER);

            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setOpaque(false);
            
            JButton editBtn = createStyledButton("Edit", new Color(41, 128, 185));
            JButton deleteBtn = createStyledButton("Delete", new Color(255, 87, 87));

            editBtn.addActionListener(e -> editUser(user));
            deleteBtn.addActionListener(e -> deleteUser(user));

            buttonPanel.add(editBtn);
            buttonPanel.add(deleteBtn);
            userPanel.add(buttonPanel, BorderLayout.EAST);

            userListPanel.add(userPanel);
        }

        revalidate();
        repaint();
    }

    private void editUser(User user) {
        JTextField nameField = new JTextField(user.getName());
        JTextField photoField = new JTextField(user.getPhotoPath());

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
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
    }

    private void deleteUser(User user) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (userDAO.deleteUser(user.getId())) {
                JOptionPane.showMessageDialog(this, "✅ User deleted.");
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to delete user.");
            }
        }
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
