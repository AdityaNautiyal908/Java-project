// File: view/ForgotPassword.java
package view;

import dao.AdminDAO;
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
    private JButton resetButton, closeButton;
    private AdminDAO adminDAO = new AdminDAO();

    public ForgotPassword(JFrame parentFrame) {
        super(parentFrame, "Forgot Password", true);
        setSize(800, 600);
        setLocationRelativeTo(parentFrame);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 800, 600, 20, 20));

        // Create main panel with gradient background
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Create content panel with BoxLayout for vertical alignment
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title label
        JLabel titleLabel = new JLabel("RESET PASSWORD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        contentPanel.add(titleLabel);

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(usernameLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        usernameField = createStyledTextField();
        usernameField.setMaximumSize(new Dimension(400, 50));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(usernameField);
        contentPanel.add(Box.createVerticalStrut(20));

        // New Password label and field
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setForeground(Color.WHITE);
        newPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        newPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(newPasswordLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        newPasswordField = createStyledPasswordField();
        newPasswordField.setMaximumSize(new Dimension(400, 50));
        newPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(newPasswordField);
        contentPanel.add(Box.createVerticalStrut(20));

        // Confirm Password label and field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        confirmPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(confirmPasswordLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        confirmPasswordField = createStyledPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(400, 50));
        confirmPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(confirmPasswordField);
        contentPanel.add(Box.createVerticalStrut(30));

        // Reset button
        resetButton = createStyledButton("Reset Password", new Color(46, 204, 113));
        resetButton.setPreferredSize(new Dimension(300, 50));
        resetButton.setMaximumSize(new Dimension(300, 50));
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addActionListener(e -> handleReset());
        contentPanel.add(resetButton);

        // Add close button
        closeButton = createStyledButton("×", new Color(231, 76, 60));
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 28));
        closeButton.setPreferredSize(new Dimension(60, 60));
        closeButton.addActionListener(e -> dispose());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.add(closeButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center the content panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(contentPanel, new GridBagConstraints());
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Add keyboard navigation
        usernameField.addKeyListener(new KeyAdapter() {
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

    private void handleReset() {
        String username = usernameField.getText();
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter your username.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
            );
            usernameField.requestFocus();
            return;
        }

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter and confirm your new password.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
            );
            newPasswordField.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
            );
            newPasswordField.requestFocus();
            return;
        }

        // Update password in database
        if (adminDAO.updatePassword(username, newPassword)) {
            JOptionPane.showMessageDialog(this,
                "Password has been reset successfully.",
                "Password Reset",
                JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to reset password. Please check your username.",
                "Reset Failed",
                JOptionPane.ERROR_MESSAGE
            );
            usernameField.requestFocus();
        }
    }
}
