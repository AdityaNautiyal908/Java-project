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

        setTitle("Fitness Hub - Admin Registration");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 800, 500, 20, 20));

        // Gradient background main panel
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(41, 128, 185),
                    0, getHeight(), new Color(44, 62, 80)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Top panel with title and close button ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel topTitle = new JLabel("Admin Registration");
        topTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        topTitle.setForeground(Color.WHITE);
        topTitle.setBorder(BorderFactory.createEmptyBorder(10, 250, 10, 0));
        topPanel.add(topTitle, BorderLayout.WEST);

        JButton closeButton = new JButton("✖");
        closeButton.setFont(new Font("Arial", Font.BOLD, 22));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(231, 76, 60));
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setContentAreaFilled(true);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(50, 40));
        closeButton.addActionListener(e -> dispose());
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        closePanel.setOpaque(false);
        closePanel.add(closeButton);
        topPanel.add(closePanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- Left image panel ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(320, 400));
        // Dummy image path, replace with your actual image path
        ImageIcon icon = new ImageIcon("resources/admin_register.png");
        Image scaledImage = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        leftPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // --- Right form panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        usernameField = createStyledTextField();
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy++; gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        passwordField = createStyledPasswordField();
        formPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy++; gbc.anchor = GridBagConstraints.EAST;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(confirmPasswordLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        confirmPasswordField = createStyledPasswordField();
        formPanel.add(confirmPasswordField, gbc);

        // Security Question
        gbc.gridx = 0; gbc.gridy++; gbc.anchor = GridBagConstraints.EAST;
        JLabel securityQuestionLabel = new JLabel("Security Question:");
        securityQuestionLabel.setForeground(Color.WHITE);
        securityQuestionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(securityQuestionLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        securityQuestionCombo = new JComboBox<>(securityQuestions);
        securityQuestionCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(securityQuestionCombo, gbc);

        // Security Answer
        gbc.gridx = 0; gbc.gridy++; gbc.anchor = GridBagConstraints.EAST;
        JLabel securityAnswerLabel = new JLabel("Security Answer:");
        securityAnswerLabel.setForeground(Color.WHITE);
        securityAnswerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(securityAnswerLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        securityAnswerField = createStyledTextField();
        formPanel.add(securityAnswerField, gbc);

        // Pet Name
        gbc.gridx = 0; gbc.gridy++; gbc.anchor = GridBagConstraints.EAST;
        JLabel petNameLabel = new JLabel("Pet Name:");
        petNameLabel.setForeground(Color.WHITE);
        petNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(petNameLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        petNameField = createStyledTextField();
        formPanel.add(petNameField, gbc);

        // Secret Code
        gbc.gridx = 0; gbc.gridy++; gbc.anchor = GridBagConstraints.EAST;
        JLabel secretCodeLabel = new JLabel("Secret Code:");
        secretCodeLabel.setForeground(Color.WHITE);
        secretCodeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(secretCodeLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        secretCodeField = createStyledTextField();
        formPanel.add(secretCodeField, gbc);

        // Register Button (centered, spans both columns)
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton registerButton = createStyledButton("Register", new Color(46, 204, 113));
        registerButton.addActionListener(e -> handleRegistration());
        formPanel.add(registerButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        // Keyboard navigation (optional, as before)
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
        textField.setBackground(new Color(255, 255, 255, 200));
        textField.setForeground(Color.BLACK);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBackground(new Color(255, 255, 255, 200));
        passwordField.setForeground(Color.BLACK);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 1, true),
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
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
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