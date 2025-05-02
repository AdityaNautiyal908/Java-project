// File: view/AdminLogin.java
package view;

import dao.AdminDAO;

import javax.swing.*;
import java.awt.*;

public class AdminLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, forgotPasswordButton;
    private AdminDAO adminDAO = new AdminDAO();

    public AdminLogin() {
        setTitle("Admin Login");
        setBounds(550, 250, 700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);  // Using absolute positioning (null layout)

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);  // Same layout for the panel
        panel.setBounds(0, 0, 700, 400);
        add(panel);

        // Admin Username label and text field
        JLabel l1 = new JLabel("Admin Username:");
        l1.setBounds(124, 89, 120, 24);
        panel.add(l1);

        usernameField = new JTextField();
        usernameField.setBounds(250, 93, 157, 20);
        panel.add(usernameField);

        // Password label and password field
        JLabel l2 = new JLabel("Password:");
        l2.setBounds(124, 124, 120, 24);
        panel.add(l2);

        passwordField = new JPasswordField();
        passwordField.setBounds(250, 128, 157, 20);
        panel.add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setForeground(new Color(46, 139, 87));
        loginButton.setBackground(new Color(176, 224, 230));
        loginButton.setBounds(199, 181, 113, 25);
        panel.add(loginButton);

        loginButton.addActionListener(e -> login());

        // Admin access only label
        JLabel l5 = new JLabel("Admin Access Only");
        l5.setFont(new Font("Tahoma", Font.PLAIN, 15));
        l5.setForeground(new Color(255, 0, 0));
        l5.setBounds(160, 230, 150, 20);
        panel.add(l5);

        // Optional: Admin icon
        ImageIcon c1 = new ImageIcon("resources/login.png");
        Image i1 = c1.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i1);
        JLabel icon = new JLabel(i2);
        icon.setBounds(480, 70, 150, 150);
        panel.add(icon);

        // Forgot Password Button
        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setForeground(Color.BLUE);
        forgotPasswordButton.setBackground(new Color(230, 230, 250));
        forgotPasswordButton.setBounds(160, 270, 150, 25);
        forgotPasswordButton.setFocusPainted(false);  // Remove focus highlight
        panel.add(forgotPasswordButton);

        // Action listener for forgot password button
        forgotPasswordButton.addActionListener(e -> new ForgotPassword(this));

        // Make sure all components are visible, then call revalidate() and repaint() on the frame itself
        panel.revalidate();
        panel.repaint();
        this.revalidate();
        this.repaint();

        // Set frame visibility
        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (adminDAO.isValidAdmin(username, password)) {
            // Show loading animation
            Loading loading = new Loading(username);
            loading.setUploading();  // Start loading animation
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials ❌");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLogin::new);
    }
}
