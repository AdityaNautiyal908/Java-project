package view;

import dao.AdminDAO;
import dao.AdminSecurityDAO;
import model.AdminSecurity;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class AdminRegistration extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> securityQuestionCombo;
    private JTextField securityAnswerField;
    private JTextField petNameField;
    private JTextField secretCodeField;
    private AdminDAO adminDAO;
    private AdminSecurityDAO adminSecurityDAO;

    private String[] securityQuestions = {
        "What is your mother's maiden name?",
        "What was your first pet's name?",
        "What is your favorite book?",
        "What was your childhood nickname?",
        "What is the name of your first school?"
    };

    public AdminRegistration() {
        adminDAO = new AdminDAO();
        adminSecurityDAO = new AdminSecurityDAO();

        setTitle("Admin Registration");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Admin Registration");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Username
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = createStyledTextField();
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = createStyledPasswordField();
        formPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = createStyledPasswordField();
        formPanel.add(confirmPasswordField, gbc);

        // Security Question
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Security Question:"), gbc);
        gbc.gridx = 1;
        securityQuestionCombo = new JComboBox<>(securityQuestions);
        securityQuestionCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(securityQuestionCombo, gbc);

        // Security Answer
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Security Answer:"), gbc);
        gbc.gridx = 1;
        securityAnswerField = createStyledTextField();
        formPanel.add(securityAnswerField, gbc);

        // Pet Name
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Pet Name:"), gbc);
        gbc.gridx = 1;
        petNameField = createStyledTextField();
        formPanel.add(petNameField, gbc);

        // Secret Code
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Secret Code:"), gbc);
        gbc.gridx = 1;
        secretCodeField = createStyledTextField();
        formPanel.add(secretCodeField, gbc);

        // Register Button
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerButton = createStyledButton("Register", new Color(46, 204, 113));
        registerButton.addActionListener(e -> handleRegistration());
        formPanel.add(registerButton, gbc);

        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Add keyboard navigation
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmPasswordField.requestFocus();
                }
            }
        });

        confirmPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    securityQuestionCombo.requestFocus();
                }
            }
        });

        // Request focus on username field
        usernameField.requestFocus();
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return passwordField;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void handleRegistration() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String securityQuestion = (String) securityQuestionCombo.getSelectedItem();
        String securityAnswer = securityAnswerField.getText().trim();
        String petName = petNameField.getText().trim();
        String secretCode = secretCodeField.getText().trim();

        // Validate inputs
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
            securityAnswer.isEmpty() || petName.isEmpty() || secretCode.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Create admin account
        if (adminDAO.createAdmin(username, password)) {
            // Add security information
            AdminSecurity adminSecurity = new AdminSecurity(
                username,
                securityQuestion,
                securityAnswer,
                petName,
                secretCode
            );

            if (adminSecurityDAO.addSecurityInfo(adminSecurity)) {
                JOptionPane.showMessageDialog(this,
                    "Registration successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
                new AdminLogin();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to add security information.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Username already exists.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
} 