// File: view/ForgotPassword.java
package view;

import dao.AdminDAO;
import dao.AdminSecurityDAO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class ForgotPassword extends JDialog {
    private JTextField usernameField;
    private JPasswordField newPasswordField, confirmPasswordField;
    private JComboBox<String> recoveryMethodCombo;
    private JTextField recoveryAnswerField;
    private JButton resetButton, closeButton;
    private AdminDAO adminDAO = new AdminDAO();
    private AdminSecurityDAO adminSecurityDAO = new AdminSecurityDAO();
    private JFrame parentFrame;

    private String[] recoveryMethods = {
        "Security Question",
        "Pet Name",
        "Secret Code"
    };

    public ForgotPassword(JFrame parentFrame) {
        super(parentFrame, "Forgot Password", true);
        this.parentFrame = parentFrame;
        setSize(800, 600);
        setLocationRelativeTo(parentFrame);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 800, 600, 20, 20));

        // Main panel with gradient background
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 10, 40));

        // --- Left image panel ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(320, 400));

        // Dummy image path, replace with your actual image path
        ImageIcon icon = new ImageIcon("resources/reset_password.png");
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
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST; // Right-align

        // Title label
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("RESET PASSWORD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        formPanel.add(titleLabel, gbc);

        // Username label and field
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        formPanel.add(usernameLabel, gbc);
        usernameField = createStyledTextField();
        usernameField.setMaximumSize(new Dimension(400, 50));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(usernameField, gbc);
        gbc.gridy++;

        // Recovery Method label and field
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JLabel recoveryMethodLabel = new JLabel("Recovery Method:");
        recoveryMethodLabel.setForeground(Color.WHITE);
        recoveryMethodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        recoveryMethodLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        formPanel.add(recoveryMethodLabel, gbc);
        gbc.gridy++;
        recoveryMethodCombo = new JComboBox<>(recoveryMethods);
        recoveryMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        recoveryMethodCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(recoveryMethodCombo, gbc);
        gbc.gridy++;

        // Recovery Answer label and field
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JLabel recoveryAnswerLabel = new JLabel("Answer:");
        recoveryAnswerLabel.setForeground(Color.WHITE);
        recoveryAnswerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        recoveryAnswerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        formPanel.add(recoveryAnswerLabel, gbc);
        gbc.gridy++;
        recoveryAnswerField = createStyledTextField();
        recoveryAnswerField.setMaximumSize(new Dimension(400, 50));
        recoveryAnswerField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(recoveryAnswerField, gbc);
        gbc.gridy++;

        // New Password label and field
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setForeground(Color.WHITE);
        newPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        newPasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        formPanel.add(newPasswordLabel, gbc);
        gbc.gridy++;
        newPasswordField = createStyledPasswordField();
        newPasswordField.setMaximumSize(new Dimension(400, 50));
        newPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(newPasswordField, gbc);
        gbc.gridy++;

        // Confirm Password label and field
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        confirmPasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        formPanel.add(confirmPasswordLabel, gbc);
        gbc.gridy++;
        confirmPasswordField = createStyledPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(400, 50));
        confirmPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(confirmPasswordField, gbc);
        gbc.gridy++;

        // Add the reset button at the bottom, right-aligned
        gbc.gridx = 1; gbc.gridy++; gbc.gridwidth = 1;
        resetButton = createStyledButton("Reset Password", new Color(46, 204, 113));
        resetButton.addActionListener(e -> handleReset());
        formPanel.add(resetButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add close button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        closeButton = createStyledButton("×", new Color(231, 76, 60));
        closeButton.setFont(new Font("Arial", Font.BOLD, 28));
        closeButton.setPreferredSize(new Dimension(60, 60));
        closeButton.addActionListener(e -> dispose());
        topPanel.add(closeButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        setContentPane(mainPanel);
        getRootPane().setDefaultButton(resetButton);

        // Add keyboard navigation
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    recoveryMethodCombo.requestFocus();
                }
            }
        });

        recoveryMethodCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateRecoveryField();
                }
            }
        });

        recoveryAnswerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    newPasswordField.requestFocus();
                }
            }
        });

        newPasswordField.addKeyListener(new KeyAdapter() {
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
                    handleReset();
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

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBackground(new Color(255, 255, 255, 200));
        passwordField.setForeground(Color.BLACK);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.selectAll();
                passwordField.setBackground(new Color(255, 255, 255, 255));
            }
            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setBackground(new Color(255, 255, 255, 200));
            }
        });
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

    private void updateRecoveryField() {
        String method = (String) recoveryMethodCombo.getSelectedItem();
        JLabel answerLabel = null;
        
        // Find the answer label in the form panel
        for (Component comp : recoveryAnswerField.getParent().getComponents()) {
            if (comp instanceof JLabel && comp != recoveryMethodCombo) {
                answerLabel = (JLabel) comp;
                break;
            }
        }
        
        if (answerLabel != null) {
            switch (method) {
                case "Security Question":
                    answerLabel.setText("Security Answer:");
                    break;
                case "Pet Name":
                    answerLabel.setText("Pet Name:");
                    break;
                case "Secret Code":
                    answerLabel.setText("Secret Code:");
                    break;
            }
        }
    }

    private void showStyledMessageDialog(Component parent, String message, String title, int messageType) {
        UIManager.put("OptionPane.background", new Color(44, 62, 80));
        UIManager.put("Panel.background", new Color(44, 62, 80));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.BOLD, 16));
        UIManager.put("Button.background", new Color(46, 204, 113));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }

    private void handleReset() {
        String username = usernameField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String recoveryMethod = (String) recoveryMethodCombo.getSelectedItem();
        String recoveryAnswer = recoveryAnswerField.getText().trim();

        if (username.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() || recoveryAnswer.isEmpty()) {
            showStyledMessageDialog(this,
                "Please fill in all fields.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showStyledMessageDialog(this,
                "Passwords do not match.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean isValidRecovery = false;
        switch (recoveryMethod) {
            case "Security Question":
                isValidRecovery = adminSecurityDAO.verifySecurityAnswer(username, recoveryAnswer);
                break;
            case "Pet Name":
                isValidRecovery = adminSecurityDAO.verifyPetName(username, recoveryAnswer);
                break;
            case "Secret Code":
                isValidRecovery = adminSecurityDAO.verifySecretCode(username, recoveryAnswer);
                break;
        }

        if (isValidRecovery) {
            if (adminDAO.updatePassword(username, newPassword)) {
                showStyledMessageDialog(this,
                    "Password has been reset successfully.",
                    "Password Reset",
                    JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
                if (parentFrame != null) {
                    parentFrame.dispose();
                    new AdminLogin();
                }
            } else {
                showStyledMessageDialog(this,
                    "Failed to reset password.",
                    "Reset Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            showStyledMessageDialog(this,
                "Invalid recovery information.",
                "Verification Failed",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
